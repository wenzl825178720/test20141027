package com.lina;

public class Case1 {
	
	public void fun(int studentNum){
		int n;
		if(studentNum%6==0){
			n=studentNum/6;
			System.out.println("ѧУ��Ҫ�������ǣ�"+n);
		}
		
		if(studentNum%6!=0){
			n=studentNum/6+1;
			System.out.println("ѧУ��Ҫ�������ǣ�"+n);
		}
	}

}
