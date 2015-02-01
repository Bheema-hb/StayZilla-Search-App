/**
 * 
 */
package com.techjini.communication.parsers;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.SparseIntArray;

import com.google.gson.Gson;
import com.techjini.communication.messages.RequestMessage;
import com.techjini.communication.messages.ResponseMessage;
import com.techjini.communication.services.CommunicationService;

/**
 * @author arun
 * 
 */
public class JSONParser {

	public ResponseMessage parseResponse(ResponseMessage model,
			String jsonResponse) {
		if (null != jsonResponse) {
			// jsonResponse=jsonResponse.trim();
			// jsonResponse = jsonResponse.replaceFirst("[", "");
			StringBuffer response = new StringBuffer(jsonResponse);
			// response.replace(response.length()-2, response.length(), "");
			// response.insert(0, "{ data:");
			// response.append("}");
			try {
				Gson gson = new Gson();
				model = gson.fromJson(response.toString(), model.getClass());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return model;
	}

	public String createGetRequest(String baseURL, RequestMessage requestModel) {

		if (requestModel != null) {
			HashMap<String, String> modelMap = createHashMapFromModel(
					requestModel, true);

			String uri = "?";

			Set<Entry<String, String>> set = modelMap.entrySet();

			Iterator<Entry<String, String>> iterator = set.iterator();

			while ((iterator.hasNext())) {
				Entry<String, String> data = iterator.next();
				try {
					if (data.getValue() != null
							&& !"email".equalsIgnoreCase(data.getKey())) {
						uri += data.getKey() + "="
								+ URLEncoder.encode(data.getValue(), "UTF-8");
					} else {
						uri += data.getKey() + "=" + data.getValue();
					}

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (iterator.hasNext()) {
					uri += "&";
				}
			}

			String url = baseURL + uri;
			return url;
		} else {
			return baseURL;
		}
	}

	public HttpDelete createDeleteRequest(String baseURL,
			RequestMessage requestModel) {
		/*
		 * HashMap<String, String> modelMap =
		 * createHashMapFromModel(requestModel, false);
		 */

		String data = null;
		// String data = createStringParam(requestModel, "&");

		data = createStringParam(requestModel, "&");

		HttpDelete deleteJob = new HttpDelete(baseURL + "?" + data);
		return deleteJob;
	}

	public HttpPost createPostRequest(String baseURL,
			RequestMessage requestModel, int bodyType) {
		/*
		 * HashMap<String, String> modelMap =
		 * createHashMapFromModel(requestModel, false);
		 */
		HttpPost postJob = new HttpPost(baseURL);

		StringEntity se = null;
		JSONObject jsonObject = new JSONObject();
		String data = null;
		// String data = createStringParam(requestModel, "&");
		// if (bodyType == CommunicationService.POST_BODY_TYPE_JSON) {
		// try {
		// jsonObject = new JSONObject(new Gson().toJson(requestModel));
		//
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		//
		// } else
		if (bodyType == CommunicationService.POST_BODY_TYPE_STRING) {
			data = createStringParam(requestModel, "&");
		}
		// else {
		// System.out.print("JsonParser: Post body Format not supported");
		// return null;
		// }
		try {
			if (bodyType == CommunicationService.POST_BODY_TYPE_OLA) {

				StringBuffer bufferString = new StringBuffer();
				Field[] fields = requestModel.getClass().getFields();
				Gson parser = new Gson();
				for (int i = 0; i < fields.length; i++) {

					if (i != 0) {
						bufferString.append('&');

					}

					try {

						String s = fields[i].getName() + "="
								+ parser.toJson(fields[i].get(requestModel));
						bufferString.append(s);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
				se = new StringEntity(bufferString.toString(), HTTP.UTF_8);

				// try {
				// se = new StringEntity(fields[0].getName() + "="
				// + new Gson().toJson(fields[0].get(requestModel)),
				// HTTP.UTF_8);
				// DLogger.e(this, "post length" + fields[0].getName() + "="
				// + new Gson().toJson(fields[0].get(requestModel)));
				// } catch (IllegalAccessException e) {
				// e.printStackTrace();
				// } catch (IllegalArgumentException e) {
				// e.printStackTrace();
				// }
			} else if (bodyType == CommunicationService.POST_BODY_TYPE_JSON) {
				try {
					jsonObject = new JSONObject(new Gson().toJson(requestModel));
					try {
						se = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (bodyType == CommunicationService.POST_BODY_TYPE_STRING) {
				se = new StringEntity(data, HTTP.UTF_8);
			} else {
				System.out.print("JsonParser: Post body Format not supported");
			}
			/*
			 * try { StringEntity se; se = new StringEntity(postData,
			 * HTTP.UTF_8); se.setContentType("application/json");
			 */

			se.setContentType("application/json");

			// LoggerElecom.d(this, data + "StringEntity :" + se);

			postJob.setEntity(se);

			// writeToFile(jsonObject.toString());
			/*
			 * HttpParams httpParameters = new BasicHttpParams();
			 * httpParameters.setParameter(name, value)
			 */
			// postJob.setParams(httpParameters);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return postJob;
	}

	// public HttpPost createPostRequest(String baseURL,
	// RequestMessage requestModel) {
	// /*
	// * HashMap<String, String> modelMap =
	// * createHashMapFromModel(requestModel, false);
	// */
	// HttpPost postJob = new HttpPost(baseURL);
	// postJob.setHeader("Accept", "application/json");
	// String data = createStringParam(requestModel, "&");
	// StringEntity se;
	// try {
	// se = new StringEntity(data, HTTP.UTF_8);
	// /*
	// * try { StringEntity se; se = new StringEntity(postData,
	// * HTTP.UTF_8); se.setContentType("application/json");
	// */
	//
	// se.setContentType("application/x-www-form-urlencoded");
	//
	//
	// postJob.setEntity(se);
	//
	// /*
	// * HttpParams httpParameters = new BasicHttpParams();
	// * httpParameters.setParameter(name, value)
	// */
	// // postJob.setParams(httpParameters);
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	//
	// return postJob;
	// }

	public String createStringParam(RequestMessage requestModel,
			String seperator) {

		HashMap<String, String> hashMap = createHashMapFromModel(requestModel,
				true);
		Set<Entry<String, String>> set = hashMap.entrySet();

		Iterator<Entry<String, String>> iterator = set.iterator();
		String param = "";
		while ((iterator.hasNext())) {
			Entry<String, String> data = iterator.next();

			param += data.getKey() + "=" + data.getValue();
			if (iterator.hasNext()) {
				param += seperator;
			}
		}
		// FLogger.d(this, "PUT :"+param );
		return param;

	}

	public HttpPut createPutRequest(String baseURL, RequestMessage requestModel) {
		/*
		 * HashMap<String, String> modelMap =
		 * createHashMapFromModel(requestModel, false);
		 */
		HttpPut putJob = new HttpPut(baseURL);
		Gson gson = new Gson();
		String data = gson.toJson(requestModel);
		// Logger.d(this, "set data = " + data);
		// HashMap<String, String> values = createHashMapFromModel(requestModel,
		// true);
		// JSONObject jsonObject = new JSONObject();
		// Iterator<Entry<String, String>> iterator =
		// values.entrySet().iterator();
		// while ((iterator.hasNext())) {
		// Entry<String, String> entitySet = iterator.next();
		// try {
		// jsonObject.put(entitySet.getKey(), entitySet.getValue());
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// }
		/*
		 * Gson gson = new Gson(); String putData = gson.toJson(requestModel);
		 * FLogger.d(this, "PutData  :" + putData);
		 */
		// String data = createStringParam(requestModel, "&");
		StringEntity se;
		try {
			se = new StringEntity(data, HTTP.UTF_8);

			se.setContentType("application/json");

			putJob.setEntity(se);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return putJob;
	}

	private HashMap<String, String> createHashMapFromModel(
			RequestMessage model, boolean excludeNull) {

		HashMap<String, String> map = new HashMap<String, String>();

		Field fields[] = model.getClass().getFields();

		String key, value = null;

		for (int i = 0; i < fields.length; ++i) {

			try {
				value = null;
				key = fields[i].getName();
				if (fields[i].getType().isPrimitive()) {
					value = String.valueOf(fields[i].get(model));
				} else {
					if (fields[i].getType().getCanonicalName()
							.compareToIgnoreCase("java.lang.String") == 0) {
						value = (String) fields[i].get(model);
					} else if (fields[i].getType().getCanonicalName()
							.equalsIgnoreCase("android.util.SparseIntArray")) {
						// value=String.valueOf(fields[i].get(model));
						SparseIntArray temp = (SparseIntArray) fields[i]
								.get(model);
						if (temp != null) {
							for (int j = 0; j < temp.size(); j++) {
								String tempKey = "" + key + "[" + j + "][0]";
								String tempvalue = String
										.valueOf(temp.keyAt(j));
								map.put(tempKey, tempvalue);
								tempKey = "" + key + "[" + j + "][1]";
								tempvalue = String.valueOf(temp.valueAt(j));
								map.put(tempKey, tempvalue);
							}
						}
						// System.out.println(fields[i].getType().getCanonicalName()+":::value"+key+"::"+value);
						value = null;
					} else if (fields[i].getType().getCanonicalName()
							.equalsIgnoreCase("java.lang.Integer")) {
						Integer valu = (Integer) fields[i].get(model);
						value = null == valu ? null : String.valueOf(valu);
					} else if (fields[i].getType().getCanonicalName()
							.equalsIgnoreCase("java.lang.Float")) {
						Float valu = (Float) fields[i].get(model);
						value = null == valu ? null : String.valueOf(valu);
					}

					else {
						System.out.println("currently not supported");
					}
				}
				// DLogger.d(this, "get parser" + "key value " + key + " " +
				// value
				// + " type " + (value == null));
				// if (value != null) {
				// DLogger.d(this, "get parser" + "string equal " + key + " "
				// + value.equalsIgnoreCase("null"));
				// }
				if (excludeNull) {
					if (value != null && !(value.equalsIgnoreCase("null")))
						map.put(key, value);
				} else {
					map.put(key, value);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

}
