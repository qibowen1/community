package com.qbwyyds.community.community.utils;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    private static final Logger logger= LoggerFactory.getLogger(SensitiveFilter.class);

    //定义替换的常亮
    private static final String REPLACEMENT="***";
    //根节点
    private TriedNode root=new TriedNode();

    //初始化前缀树
    @PostConstruct //在容器初始化之后（服务器启动后 就初始化）
    private void init() throws IOException {
        InputStream resourceAsStream=null;
        //字节流 -》字符流-》缓冲流
        BufferedReader bufferedReader=null;
        String key;
        try{
            resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
            while ((key=bufferedReader.readLine())!=null){
                this.addKeyWorld(key);
            }
        }catch (Exception e){
            logger.error("读取敏感词文件失败"+e.getMessage());
        }finally {
            if (resourceAsStream!=null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (bufferedReader!=null)
                bufferedReader.close();
        }
    }

    private void addKeyWorld(String key){
        TriedNode temp= root;
        for (int i=0;i<key.length();i++){
            char c= key.charAt(i);
            TriedNode subNode = temp.getSubNode(c);
            //判断是否有子节点
            if (subNode==null){
                subNode=new TriedNode();
                temp.addSubNodes(c,subNode);
            }
            temp=subNode;

            //设置结束标志
            if (i==key.length()-1){
                temp.setKeyWorldEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text
     * @return
     */
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return null;
        }
        int begin=0;
        int position=0;
        TriedNode tempNode=root;

        //结果
        StringBuffer sb=new StringBuffer();

        while (position<text.length()){
            char c=text.charAt(position);

            //跳过特殊符号
            if (isSymbol(c)){
                if (tempNode==root){
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            //检查下级符号
            tempNode=tempNode.getSubNode(c);
            if (tempNode==null){
                //以begin开头的不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position=++begin;
                tempNode=root;
            }else if (tempNode.isKeyWorldEnd()){
                //发现敏感词
                sb.append(REPLACEMENT);
                begin=++position;
            }else {
                //检查下一个字符
                position++;
            }
        }
        //还要最后一段字符
        sb.append(text.substring(begin));
        return sb.toString();
    }

    private boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c)&&(c < 0x2E80 || c > 0x9FFF);//如果是特殊字符，返回flase
    }

    private class TriedNode{
        //关键字结束付
        private boolean isKeyWorld= false;
        //用map作为节点结构
        Map<Character, TriedNode> subNodes= new HashMap<>();

        public boolean isKeyWorldEnd(){
            return isKeyWorld;
        }

        public void setKeyWorldEnd(boolean keyWorld){
            this.isKeyWorld=keyWorld;
        }

        //添加子节点
        public void addSubNodes(Character c, TriedNode node){
            subNodes.put(c,node);
        }
        //获取子节点

        public TriedNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
