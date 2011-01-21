package it.crazybiz.server.domain.dao;

public class DAOFactory {

	private static DAOFactory instance;
	
	private DAOFactory(){
		
	}
	
	public static DAOFactory getInstance(){
		if (instance==null){
			instance = new DAOFactory();
		}
		return instance;
	}
	
	public DAO getBrandDAO(){
		
	}
	
	public DAO getItemDAO(){
		
	}
	
	public DAO getModelDAO(){
		
	}
	
	public DAO getPaymentDAO(){
		
	}
	
	public DAO getPersonDAO(){
		
	}
	
	public DAO getPostDAO(){
		
	}
	
	public DAO getProposalDAO(){
		
	}
	
	public DAO getPurchaseDAO(){
		
	}
	
	public DAO getSaleDAO(){
		
	}
	
	public DAO getShipmentDAO(){
		
	}
	
	public DAO getWatchedDAO(){
		
	}
}
