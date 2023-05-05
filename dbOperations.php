<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
class DbOperation
{
    //Database connection link
    private $con;
 
    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/dbConnect.php';
 
        //Creating a DbConnect object to connect to the database
        $db = new dbConnect();
 
        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
    }
 
    function createUser($name, $realname, $rating, $position){
        $stmt = $this->con->prepare("INSERT INTO mem(name, realname, rating, position) VALUES (?, ?, ?, ?)");
        if (!$stmt) {
            die("Error during statement preparation: " . $this->con->error);
        }
        $stmt->bind_param("ssis", $name, $realname, $rating, $position);
        if (!$stmt->execute()) {
            die("Error during statement execution: " . $stmt->error);
        }
        return true;
    }
        
}