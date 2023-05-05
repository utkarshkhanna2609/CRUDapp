<?php

require_once '../includes/dbOperations.php';

$response=array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    if(
        isset($_POST['name']) and isset($_POST['realname']) and isset($_POST['rating']) and isset($_POST['position'])){
            $db=new dbOperation();
            if($db->createUser($_POST['name'],$_POST['realname'], $_POST['rating'], $_POST['position'])){
                $response['error']=false;
                $response['message']="registration success";
            }else{
                $response['error']=true;
                $response['message']="error occured, please try again";
            }
    }else{
        $response['error']=true;
        $response['message']="missing required data";
    }
}else{
    $response['error']=true;
    $response['message']="Invalid Request";
}

echo json_encode($response);
