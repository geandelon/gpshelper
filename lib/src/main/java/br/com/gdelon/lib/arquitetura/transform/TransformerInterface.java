package br.com.gdelon.lib.arquitetura.transform;

import android.database.Cursor;

import br.com.gdelon.lib.model.BaseModel;

/**
 * Created by geandelon on 23/03/2016.
 */
public interface TransformerInterface<M extends BaseModel> {

    M toModelo(Cursor cursor);

}
