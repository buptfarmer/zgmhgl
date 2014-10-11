package com.zhy.csdn;

import com.zhy.bean.CommonException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataUtil
{
//    public static String doGet(String urlStr) throws CommonException  
//    {  
//        StringBuffer sb = new StringBuffer();  
//        try  
//        {  
//            URL url = new URL(urlStr);  
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
//            conn.setRequestMethod("GET");  
//            conn.setConnectTimeout(5000);  
//            conn.setDoInput(true);  
//            conn.setDoOutput(true);  
//  
//            if (conn.getResponseCode() == 200)  
//            {  
//                InputStream is = conn.getInputStream();  
//                InputStreamReader isr = new InputStreamReader(is,"UTF-8");  
//                int len = 0;  
//                char[] buf = new char[1024];  
//  
//                while ((len = isr.read(buf)) != -1)  
//                {  
//                    sb.append(new String(buf, 0, len));  
//                }  
//  
//                is.close();  
//                isr.close();  
//            } else  
//            {  
//                throw new CommonException("��������ʧ�ܣ�");  
//            }  
//  
//        } catch (Exception e)  
//        {  
//            throw new CommonException("��������ʧ�ܣ�");  
//        }  
//        return sb.toString();  
//    }  
	/**
	 * ���ظ����ӵ�ַ��html���
	 * 
	 * @param urlStr
	 * @return
	 * @throws com.zhy.bean.CommonException
	 */
	public static String doGet(String urlStr) throws CommonException
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setDoInput(true);
//			conn.setDoOutput(true);
//			conn.setRequestProperty("Charset", "utf-8");

			int responseCode = conn.getResponseCode();
			System.out.println("requestUrl:"+urlStr);

			System.out.println("responseCode:"+responseCode);
          if (responseCode== 200)  
          {  
              InputStream is = conn.getInputStream();
              InputStreamReader isr = new InputStreamReader(is,"utf-8");
              int len = 0;  
              char[] buf = new char[1024];  

              while ((len = isr.read(buf)) != -1)  
              {  
                  sb.append(new String(buf, 0, len));
              }  

              is.close();  
              isr.close();  
          } else  
          {  
              throw new CommonException("��������ʧ�ܣ�");
          }  
//			if (conn.getResponseCode() == 200)
//			{
//				InputStream is = conn.getInputStream();
//				int len = 0;
//				byte[] buf = new byte[1024];
//
//				while ((len = is.read(buf)) != -1)
//				{
//					System.out.println("GET:"+new String(buf, 0, len, "gb2312"));
//					sb.append(new String(buf, 0, len, "gb2312"));
//				}
//
//				is.close();
//			} else
//			{
//				throw new CommonException("��������ʧ�ܣ�");
//			}

		} catch (Exception e)
		{
			throw new CommonException("��������ʧ�ܣ�");
		}
		return sb.toString();
	}

	
	

}
