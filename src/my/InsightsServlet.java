package my;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Servlet implementation class InsightsServlet
 */
@WebServlet("/InsightsServlet")
public class InsightsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InsightsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/**
		 *  文字コードの設定
		 */
		request.setCharacterEncoding("UTF-8");

		/**
		 *  HTTPパラメータの取得
		 */
		String trgData = request.getParameter("data");

		/**
		 *  環境変数の取得
		 */
		String vcapData = System.getenv("VCAP_SERVICES");
		String piUrl = null;
		String piPassword = null;
		String piUsername = null;

		/**
		 *  JSONの解析
		 */
		JSONParser parser = new JSONParser();

		/**
		 *  Personality Insightsの情報取得
		 */
		try{
			JSONObject obj = ( JSONObject )parser.parse( vcapData );
			
			//. PersonalityInsights
			JSONArray pis_array = ( JSONArray )obj.get( "personality_insights" );
			if( pis_array != null && pis_array.size() > 0 ){
				JSONObject pi = ( JSONObject )pis_array.get( 0 ); //. 最初の１つを見る
				JSONObject credentials = ( JSONObject )pi.get( "credentials" );
				piUrl = ( String )credentials.get( "url" );
				piUsername = ( String )credentials.get( "username" );
				piPassword = ( String )credentials.get( "password" );
			}
		}catch( Exception e ){
			e.printStackTrace();
		}

		/**
		 *  Credentialが取得できた場合、Personal Insightsへリクエスト
		 */
		if (piUrl != null && piUsername != null && piPassword != null) {
			String postResult = null;
			String[] piResult = new String[3]; //. Big5 / Needs / Values
			String urlStem = piUrl + "/v2/profile";
			try {
				/**
				 *  POST処理
				 */
				String wordCountMessage = null;
				postResult = getData(piUsername, piPassword, urlStem, trgData);
				
				if (postResult != null) {
					/**
					 *  JSONの解析
					 */
					try{
						JSONObject obj = ( JSONObject )parser.parse( postResult );
						wordCountMessage = (String) obj.get("word_count_message");
						JSONObject tree = ( JSONObject )obj.get( "tree" );
						JSONArray children = ( JSONArray )tree.get( "children" );
						for( int i = 0; i < children.size(); i ++ ){
							JSONObject child = ( JSONObject )children.get( i );
							piResult[i] = child.toJSONString();
						}
					}catch( Exception e ){
					}

					/**
					 *  JSPへの受け渡し
					 */
					if( wordCountMessage != null ){
						request.setAttribute("message", "処理が正常に終了しました。<br/>" + wordCountMessage + "<br/><br/>");			
						request.setAttribute("resultText", postResult);
						request.setAttribute("resultBig5", piResult[0]);
						request.setAttribute("resultNeeds", piResult[1]);
						request.setAttribute("resultValues", piResult[2]);
					}else{
						request.setAttribute("message", "処理が失敗しました。入力テキストの量が不十分だった可能性があります。");								
					}
				} else {
					request.setAttribute("message", "Personal Insightsのレスポンス情報が不正です。");								
				}
			} catch (Exception e) {
				e.printStackTrace(System.err);
				request.setAttribute("message", "Personal Insightsの処理でエラーが発生しました。");
			}
		} else {
			request.setAttribute("message", "Personal InsightsのCredential情報が取得できません。");			
		}
		
		/**
		 *  JSPへディスパッチ
		 */
		RequestDispatcher dispatch = request.getRequestDispatcher("/index.jsp");
		dispatch.forward(request, response);
	}

	/**
	 * HTTP POSTリクエスト
	 */
	private static String getData(String username, String password, String urlStem, String postData) throws IOException {
		// URL、HTTPメソッドの設定
		URL url = new URL(urlStem);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setInstanceFollowRedirects(false);

		// HTTPヘッダの設定
		con.setRequestProperty("Accept-Language", "jp");
		con.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
		con.setRequestProperty("Content-Language", "ja");
		byte[] b64data = Base64.encodeBase64( ( username + ":" + password ).getBytes() );
		con.setRequestProperty("Authorization", "Basic " + new String( b64data ) );

		// HTTP接続
		con.connect();

		// POSTデータの送信
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "utf-8")));
		pw.print(postData);
		pw.close();

		// HTTPレスポンスのBODY取得
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		} catch (Exception e_) {
			System.out.println(con.getResponseCode() + " " + con.getResponseMessage());
			return con.getResponseCode() + " " + con.getResponseMessage();
		}
		String line;
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line);
			System.out.println(line + "\n");
		}		

		// HTTP切断
		con.disconnect();

		return sb.toString();
	}
}
