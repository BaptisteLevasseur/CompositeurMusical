package com.example.compositeurmusical;

public class FichierSon {
	
	String titre;
	long duree;
	long id;
	
	public FichierSon(long id, long duree, String titre){
		this.titre=titre;
		this.duree=duree;
		this.id=id;
	}
	
	public String getTitre(){
		return this.titre;
	}
	
	public Long getDuree(){
		return this.duree;
	}
	
	public Long getId(){
		return this.id;
	}
	
	public void setTitre(String unTitre){
		this.titre=unTitre;
	}
	
	public void setDuree(Long uneDuree){
		this.duree=uneDuree;
	}
	
	public void setId(long unId){
		this.id=unId;
	}

}
