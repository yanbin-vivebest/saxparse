package com.vivebest.xmlparser.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class FileUtil
{
  public static Properties loadResourceFile(String resourceFile)
    throws IOException
  {
    Properties prop = new Properties();
    InputStream is = new FileInputStream(new File(resourceFile));
    prop.load(is);
    if (is != null) {
      is.close();
    }
    return prop;
  }
  
}
