package it.crazybiz.server.domain.dao;

public interface DAO<ObjectType> {
	public int create(ObjectType element);
	public ObjectType retrieve(int id);
	public void update(int id, ObjectType newElement);
	public void delete(int id);
}
