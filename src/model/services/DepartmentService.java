package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;


public class DepartmentService {

	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll() {
		return dao.findAll();		
	}
	
	public void saveOrUpdate(Department department) {
		//se o id for nulo o objeto dao vai inserir um departamento
		if(department.getId()==null) {
			dao.insert(department);
		}
		//caso ja exista no banco de dados, ele irá atualizar
		else {
			dao.update(department);
		}
	}
	
	
}
