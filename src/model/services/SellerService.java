package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {

	private SellerDao dao = DaoFactory.createSellerDao();

	public List<Seller> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Seller seller) {
		// se o id for nulo o objeto dao vai inserir um departamento
		if (seller.getId() == null) {
			dao.insert(seller);
		}
		// caso ja exista no banco de dados, ele irá atualizar
		else {
			dao.update(seller);
		}
	}
	
	public void remove(Seller seller) {
		dao.deleteById(seller.getId());
	}

}
