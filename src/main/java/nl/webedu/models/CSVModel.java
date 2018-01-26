/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.models;

/**
 *
 * @author RdenBlaauwen
 */
public class CSVModel {
    private String content;
    
    public CSVModel(String content){
        this.content=content;
    }
    
    public String getContent(){
        return this.content;
    }
    public void setContent(String newContent){
        this.content=newContent;
    }
}
