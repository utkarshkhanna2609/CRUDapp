<?php
    class dbConnect{
        private $con;
        function _construct(){

        }
        function connect(){
            include_once dirname(__FILE__).'/constants.php';
            $this->con=new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);
            if(mysqli_connect_errno()){
                echo "Failed to connect".mysqli_connect_error();
            }
            return $this->con;
        }
    }