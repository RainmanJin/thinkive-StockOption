package com.thinkive.market.service.works;

 

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thinkive.base.util.DateHelper;
import com.thinkive.base.util.StringHelper;
import com.thinkive.market.Library;


public class TaskConfig
{
    //单子属性对象
    private static TaskConfig instance = new TaskConfig();

    //保存每一个任务的配置文件
    private static ArrayList taskList = new ArrayList();

    private static Logger logger = Logger.getLogger(TaskConfig.class);
    private static String CONFIG_FILE_NAME = "tasks.xml";

    static
    {
        loadConfig();
    }

    /**
     * 读入配置文件
     *
     * @param
     */
    private static void loadConfig()
    {
        try
        {
            InputStream is = TaskConfig.class.getResourceAsStream("/" + CONFIG_FILE_NAME);
            SAXReader reader = new SAXReader();

            Document document = reader.read(is);
            Element tasksElement = document.getRootElement();
            List taskElementList = tasksElement.elements("task");

            Iterator taskElementIter = taskElementList.iterator();
            while (taskElementIter.hasNext())
            {
                Element taskElement = (Element) taskElementIter.next();
                HashMap taskPropertyMap = new HashMap();

                //获得任务的ID
                String taskId = taskElement.attributeValue("id" );
                //若ID为空，则跳过该任务配置
                if (StringHelper.isEmpty(taskId))
                {
                    continue;
                }
                taskPropertyMap.put("id", taskId);

                //获得所有的属性
                List taskPropertyList = taskElement.elements();
                Iterator taskPropertyElementIter = taskPropertyList.iterator();
                while (taskPropertyElementIter.hasNext())
                {
                    Element propertyElement = (Element) taskPropertyElementIter.next();
                    String name = propertyElement.getName();
                    String value =  propertyElement.getTextTrim() ;
                    taskPropertyMap.put(name, value);
                }
                taskList.add(taskPropertyMap);
            }
        }
        catch (DocumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }


    public static List getTaskList()
    {
        return taskList;
    }
    
    /**
     * @描述：
     * @作者：岳知之
     * @时间：2012-3-15 下午3:54:19
     * @param args
     */
    public static void main(String[] args)
    {
        List list=TaskConfig.getTaskList();
        System.out.println(list);
        HashMap task=(HashMap)list.get(0);
        System.out.println(DateHelper.parseString((String)task.get("task-begintime"), "HH:mm"));
        long timpTmp=System.currentTimeMillis()%(24*60*60*1000);
        long time2=DateHelper.parseString((String)task.get("task-begintime"), "HH:mm").getTime();
        Calendar c = Calendar.getInstance();
        int date=c.get(Calendar.DATE);
        int month=c.get(Calendar.MONTH);
        int year=c.get(Calendar.YEAR);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE),0,0,0);
        
        System.out.println(c.get(Calendar.DATE));
        System.out.println(new Date(c.getTimeInMillis()));
    }
}