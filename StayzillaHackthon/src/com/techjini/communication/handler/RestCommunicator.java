/**
 * 
 */
package com.techjini.communication.handler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.techjini.communication.messages.ResponseMessage;
import com.techjini.communication.services.CommunicationListener;

public class RestCommunicator {
	private CommunicationListener connectionStatusListener;
	private HttpUriRequest httpUriRequest;
	private final String NETWORK_ISSUE_MSG = "Please check your internet connection and try again later.";
	private HttpParams httpParameters;

	public void communicate(String serviceID, HttpUriRequest request,
			CommunicationListener listener, ResponseMessage model) {
		this.connectionStatusListener = listener;
		this.httpUriRequest = request;

		// httpUriRequest.addHeader("Accept", "application/json");
		// httpUriRequest.setHeader("Content-Type",
		// "application/x-www-form-urlencoded");
		HttpResponse response = null;

		try {

			httpParameters = new BasicHttpParams();
			int timeoutConnection = 30000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 30000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = getNewHttpClient();
			/**
			 * Avoiding the retry
			 */
			httpClient
					.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(
							0, false));
			response = httpClient.execute(httpUriRequest);
			// httpClient.getConnectionManager().closeExpiredConnections();
			// httpClient.getConnectionManager().closeIdleConnections(15,
			// TimeUnit.SECONDS);
			int errorCode = response.getStatusLine().getStatusCode();

			if (model == null) {
				model = new ResponseMessage();
			}

			model.http_code = errorCode;

			if (errorCode == 200) {
				connectionStatusListener.onSuccess(serviceID, httpUriRequest
						.getURI().toString(),
						response.getEntity().getContent(), model);
			} else {

				Gson gson = new Gson();
				// String theString = CommunicationHandler
				// .getStringFromInputStream(response.getEntity()
				// .getContent());
				// DLogger.e(this, "response : input " + theString);
				JsonReader reader = new JsonReader(new InputStreamReader(
						response.getEntity().getContent()));
				try {
					model = gson.fromJson(reader, model.getClass());
				} catch (Exception e) {
					e.printStackTrace();
					model = new ResponseMessage();
					model.http_code = errorCode;
					model.message = "server is busy try again later";
				} finally {
					reader.close();
				}
				if (model == null) {
					model = new ResponseMessage();
					model.http_code = errorCode;
					model.message = "server is busy try again later";
				}
				connectionStatusListener.onError(serviceID, model.http_code,
						model.message, model);

			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			connectionStatusListener.onError(serviceID, 111, NETWORK_ISSUE_MSG,
					model);
		} catch (ConnectException e) {
			e.printStackTrace();
			connectionStatusListener
					.onError(
							serviceID,
							121,
							"The server is taking too long to respond. Please try again later.",
							model);
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			connectionStatusListener
					.onError(
							serviceID,
							122,
							"The server is taking too long to respond. Please try again later.",
							model);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			connectionStatusListener.onError(serviceID, 123, NETWORK_ISSUE_MSG,
					model);
		} catch (InterruptedIOException e) {
			e.printStackTrace();
			connectionStatusListener.onError(serviceID, 124, NETWORK_ISSUE_MSG,
					model);
		} catch (SocketException e) {
			e.printStackTrace();
			connectionStatusListener
					.onError(
							serviceID,
							125,
							"The server is taking too long to respond. Please try again later.",
							model);
		} catch (IOException e) {
			e.printStackTrace();
			connectionStatusListener.onError(serviceID, 126, NETWORK_ISSUE_MSG,
					model);

		}
		try {
			if (response != null) {
				response.getEntity().consumeContent();
				response = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	private class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	public DefaultHttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					httpParameters, registry);

			return new DefaultHttpClient(ccm, httpParameters);
		} catch (Exception e) {
			e.printStackTrace();
			return new DefaultHttpClient();
		}
	}

}
