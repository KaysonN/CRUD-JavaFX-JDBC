package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	// lista de objetos do tipo DataChangeListener
	private List<DataChangeListener> dataChangeListenersList = new ArrayList<DataChangeListener>();

	// dependencia para o departamento
	private Seller entity;

	private SellerService service;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setSellerService(SellerService service) {
		this.service = service;
	}

	// metodo para permitir que outros objetos se inscrevam na lista e receberem o
	// evento
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListenersList.add(listener);
	}

	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			// departamento declarado no controller
			entity = getFormData();

			// salvo no banco de dados
			service.saveOrUpdate(entity);

			notifyDataChangeListeners();

			// fecha janela
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener dataChangeListener : dataChangeListenersList) {
			dataChangeListener.onDataChanged();
		}
	}

	// responsável por pegar os dados das caixinhas do form e instanciar um
	// departamento
	private Seller getFormData() {
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("Validation Error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Campo não pode ser vazio.");
		}
		obj.setName(txtName.getText());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		// fecha janela
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initializeNodes();

	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(String.valueOf(entity.getName()));
	}

	private void setErrorMessages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
}
