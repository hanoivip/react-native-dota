package org.cocos2dx.sdk;

import android.app.Activity;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.ClipboardManager;

public class DTClip
{
  private static DTConfigure config;
  private static ClipboardManager mClipboardManager = null;
  
  public DTClip(DTConfigure paramDTConfigure)
  {
    config = paramDTConfigure;
    if (Build.VERSION.SDK_INT >= 11)
    {
      mClipboardManager = (ClipboardManager)config.getActivity().getSystemService("clipboard");
      return;
    }
    mClipboardManager = (ClipboardManager)config.getActivity().getSystemService("clipboard");
  }
  
  public void copyStringToSystem(String paramString)
  {
    try
    {
      if (Build.VERSION.SDK_INT >= 11)
      {
        mClipboardManager.setText(paramString);
        return;
      }
      mClipboardManager.setText(paramString);
      return;
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
    }
  }
}


/* Location:              D:\GAMES\android\dex2jar-2.0\classes-dex2jar.jar!\org\cocos2dx\sdk\DTClip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */