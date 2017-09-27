package br.com.gdelon.lib.model.persistencia;

/**
 * Created by geandelon on 12/06/2017.
 */

public abstract class Query {

    public static class Rastreamento {

        public static final String SELECT_NAO_ENVIADOS_DO_USUARIO =
                ""
                        + " SELECT  id, codigo_usuario, hash, latitude, longitude, velocidade   "
                        + "         , acuracia, data_hora_captura, data_hora_envio, distancia   "
                        + "         , informacoes_adicionais                                    "
                        + " FROM    GPS_RASTREAMENTO                                            "
                        + " WHERE   codigo_usuario = ?                                          "
                        + "         AND data_hora_envio is null                                 "
                ;

        public static final String SELECT_NAO_ENVIADOS_DO_USUARIO_LIMIT =
                ""
                        + " SELECT  id, codigo_usuario, hash, latitude, longitude, velocidade   "
                        + "         , acuracia, data_hora_captura, data_hora_envio, distancia   "
                        + "         , informacoes_adicionais                                    "
                        + " FROM    GPS_RASTREAMENTO                                            "
                        + " WHERE   codigo_usuario = ?                                          "
                        + "         AND data_hora_envio is null                                 "
                        + "         ORDER BY data_hora_captura DESC                             "
                        + "         LIMIT ?                                                     "
                ;

        public static final String SELECT_NAO_ENVIADOS_DO_USUARIO_LIMIT_ORDER_ASC =
                ""
                        + " SELECT  id, codigo_usuario, hash, latitude, longitude, velocidade   "
                        + "         , acuracia, data_hora_captura, data_hora_envio, distancia   "
                        + "         , informacoes_adicionais                                    "
                        + " FROM    GPS_RASTREAMENTO                                            "
                        + " WHERE   codigo_usuario = ?                                          "
                        + "         AND data_hora_envio is null                                 "
                        + "         ORDER BY data_hora_captura                                  "
                        + "         LIMIT ?                                                     "
                ;


        public static final String SELECT_ULTIMAS_POSICOES =
                ""
                        + " SELECT  id, codigo_usuario, hash, latitude, longitude, velocidade   "
                        + "         , acuracia, data_hora_captura, data_hora_envio, distancia   "
                        + "         , informacoes_adicionais                                    "
                        + " FROM    GPS_RASTREAMENTO                                            "
                        + " WHERE   codigo_usuario = ?                                          "
                        + "         ORDER BY data_hora_captura DESC                             "
                        + " LIMIT    ?                                                          "
                ;



        public static final String SELECT_ULTIMAS_POSICOES_ENVIADAS =
                ""
                        + " SELECT  id, codigo_usuario, hash, latitude, longitude, velocidade   "
                        + "         , acuracia, data_hora_captura, data_hora_envio, distancia   "
                        + "         , informacoes_adicionais                                    "
                        + " FROM    GPS_RASTREAMENTO                                            "
                        + " WHERE   codigo_usuario = ?  " +
                        "           AND " +
                        "           data_hora_envio is not null                                 "
                        + "         ORDER BY data_hora_captura DESC                             "
                        + " LIMIT    ?                                                          "
                ;

        public static final String SELECT_ULTIMA_POSICAO =
                ""
                        + " SELECT  id, codigo_usuario, hash, latitude, longitude, velocidade   "
                        + "         , acuracia, data_hora_captura, data_hora_envio, distancia   "
                        + "         , informacoes_adicionais                                    "
                        + " FROM    GPS_RASTREAMENTO                                            "
                        + " WHERE   codigo_usuario = ?                                          "
                        + "         ORDER BY data_hora_captura DESC                             "
                        + " LIMIT    1                                                          "
                ;

        public static final String DELETE_WHERE_CLAUSE_RASTREAMENTO_ANTIGOS_ENVIADOS =
                "" +
                        "    codigo_usuario = ?  " +
                        "    AND   data_hora_envio IS NOT NULL" +
                        "    AND   data_hora_envio < ? ";
    }

    public static class Evento {
        public static final String SELECT_NAO_ENVIADOS_DO_USUARIO =
                ""
                        + " SELECT  id, codigo_usuario,tipo, hash, latitude, longitude,   "
                        + "         data_hora_captura, data_hora_envio, informacoes_adicionais  "
                        + " FROM    GPS_EVENTO                                                  "
                        + " WHERE   codigo_usuario = ?                                          "
                        + "         AND data_hora_envio is null                                 ";



        public static final String SELECT_NAO_ENVIADOS_DO_USUARIO_LIMIT =
                ""
                        + " SELECT  id, codigo_usuario,tipo, hash, latitude, longitude,   "
                        + "         data_hora_captura, data_hora_envio, informacoes_adicionais  "
                        + " FROM    GPS_EVENTO                                                  "
                        + " WHERE   codigo_usuario = ?                                          "
                        + "         AND data_hora_envio is null                                 "
                        + "         ORDER BY data_hora_captura DESC                             "
                        + "         LIMIT ?                                                     "
                ;

        public static final String SELECT_NAO_ENVIADOS_DO_USUARIO_LIMIT_ORDER_ASC =
                ""
                        + " SELECT  id, codigo_usuario,tipo, hash, latitude, longitude,   "
                        + "         data_hora_captura, data_hora_envio, informacoes_adicionais  "
                        + " FROM    GPS_EVENTO                                                  "
                        + " WHERE   codigo_usuario = ?                                          "
                        + "         AND data_hora_envio is null                                 "
                        + "         ORDER BY data_hora_captura                                  "
                        + "         LIMIT ?                                                     "
                ;

        public static final String DELETE_WHERE_CLAUSE_EVENTOS_ANTIGOS_ENVIADOS =
                "" +
                "    codigo_usuario = ?  " +
                "    AND   data_hora_envio IS NOT NULL" +
                "    AND   data_hora_envio < ? ";


    }

}
