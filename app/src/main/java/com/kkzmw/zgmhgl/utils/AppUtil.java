package com.kkzmw.zgmhgl.utils;

import android.content.Context;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author zhy
 * 
 */
public class AppUtil
{
	/**
	 * ����������ͻ�ȡ�ϴθ��µ�ʱ��
	 * 
	 * @param newType
	 * @return
	 */
	public static String getRefreashTime(Context context, int newType)
	{
		String timeStr = PreferenceUtil.readString(context, "NEWS_" + newType);
		if (TextUtils.isEmpty(timeStr))
		{
			return "�Һñ��������...";
		}
		return timeStr;
	}
	
	
	/**
	 * ����������������ϴθ��µ�ʱ��
	 * 
	 * @param newType
	 * @return
	 */
	public static void setRefreashTime(Context context, int newType)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		PreferenceUtil.write(context, "NEWS_" + newType,df.format(new Date()));
	}
}
