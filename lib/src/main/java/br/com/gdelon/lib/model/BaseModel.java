package br.com.gdelon.lib.model;

/**
 * @author geandelon
 */
public abstract class BaseModel {

    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BaseModel() {
        this.id = 0;
    }

    public BaseModel(int id) {
        this.id = id;
    }

    public abstract String getNomeTabela();
}
