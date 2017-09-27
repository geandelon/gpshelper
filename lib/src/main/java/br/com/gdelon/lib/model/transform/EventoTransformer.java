package br.com.gdelon.lib.model.transform;

import android.database.Cursor;

import org.joda.time.DateTime;

import br.com.gdelon.lib.arquitetura.persistencia.BaseMap;
import br.com.gdelon.lib.arquitetura.transform.TransformerInterface;
import br.com.gdelon.lib.arquitetura.utilitario.UtilData;
import br.com.gdelon.lib.model.Evento;

/**
 * Created by geandelon on 12/06/2017.
 */

public class EventoTransformer implements TransformerInterface<Evento> {

    @Override
    public Evento toModelo(Cursor cursor) {
        Evento evento = new Evento();

        int idColunaId = cursor.getColumnIndex(BaseMap.ID_RASTREAMENTO_MAP);
        if (idColunaId == -1) {
            idColunaId = cursor.getColumnIndex(BaseMap.ID_MAP);
        }

        if (idColunaId != -1) {
            evento.setId(cursor.getInt(idColunaId));
        }

        if (cursor.getColumnIndex(BaseMap.CODIGO_USUARIO_MAP) != -1) {
            evento.setCodigoUsuario(cursor.getString(cursor.getColumnIndex(BaseMap.CODIGO_USUARIO_MAP)));
        }

        if (cursor.getColumnIndex(BaseMap.TIPO_MAP) != -1) {
            evento.setTipo(cursor.getInt(cursor.getColumnIndex(BaseMap.TIPO_MAP)));
        }

        if (cursor.getColumnIndex(BaseMap.HASH_MAP) != -1) {
            evento.setHash(cursor.getString(cursor.getColumnIndex(BaseMap.HASH_MAP)));
        }

        if (cursor.getColumnIndex(BaseMap.INFORMACOES_ADICIONAIS_MAP) != -1) {
            evento.setInformacoesAdicionais(cursor.getString(cursor.getColumnIndex(BaseMap.INFORMACOES_ADICIONAIS_MAP)));
        }

        if (cursor.getColumnIndex(BaseMap.LATITUDE_MAP) != -1) {
            evento.setLatitude(
                    cursor.getDouble(cursor.getColumnIndex(BaseMap.LATITUDE_MAP)));
        }

        if (cursor.getColumnIndex(BaseMap.LONGITUDE_MAP) != -1) {
            evento.setLongitude(
                    cursor.getDouble(cursor.getColumnIndex(BaseMap.LONGITUDE_MAP)));
        }

        if (cursor.getColumnIndex(BaseMap.DATA_HORA_CAPTURA_MAP) != -1) {
            evento.setDataHoraCaptura(
                    (DateTime) UtilData.getDateTime(cursor.getString(cursor.getColumnIndex(BaseMap.DATA_HORA_CAPTURA_MAP))));
        }

        if (cursor.getColumnIndex(BaseMap.DATA_HORA_ENVIO_MAP) != -1) {
            evento.setDataHoraEnvio(
                    (DateTime) UtilData.getDateTime(cursor.getString(cursor.getColumnIndex(BaseMap.DATA_HORA_ENVIO_MAP))));
        }

        return evento;
    }

}
