package com.example.examen2p.Configuration;

public class Configuration {
    private static final String Server_http = "https://";
    private static final String Server_direction = "educationsofthn.com/";
    private static final String Web_api = "API_G5/";
    private static final String Get_all_contacts = "api/read.php";
    private static final String Get_single_contact = "api/single_read.php";
    private static final String Create_contact = "api/create.php";
    private static final String Update_contact = "api/update.php";
    private static final String Delete_contact = "api/delete.php";

    public static final String Endpoint_get_all_contacts = Server_http + Server_direction + Web_api + Get_all_contacts;
    public static final String Endpoint_get_single_contact = Server_http + Server_direction + Web_api + Get_single_contact;
    public static final String Endpoint_create_contact = Server_http + Server_direction + Web_api + Create_contact;
    public static final String Endpoint_update_contact = Server_http + Server_direction + Web_api + Update_contact;
    public static final String Endpoint_delete_contact = Server_http + Server_direction + Web_api + Delete_contact;
}
