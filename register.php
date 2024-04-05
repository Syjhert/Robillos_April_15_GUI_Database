<?php
include 'connect.php';
require_once 'includes/header.php';
?>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Accord Register Page</title>
  <link rel="stylesheet" href="css/register.css" />
  <link rel="stylesheet" href="css/nav.css" />
  <link rel="stylesheet" href="css/message.css" />

</head>

<body>


  <!-- <?php $fname = $_POST['txtfirstname'];
        $email = $_POST['txtemail'];
        $displayName = $_POST['txtdisplayname'];
        $uname = $_POST['txtusername'];
        $bday = $_POST['txtbirthday'];

        //for tbluseraccount
        $pword = $_POST['txtpassword'];

        ?> -->

  <div id="registerForm">
    <form method="post">
      <h1>Create an account</h1>
      <label for="email">Email address</label>
      <input id="email" name="txtemail" class="txtInput" type="email" placeholder="abcd@email.com" required />
      <label for="display_name">Display name</label>
      <input id="display_name" name="txtdisplayname" class="txtInput" type="text" placeholder="AccordMod" required />
      <label for="username">Username</label>
      <input id="username" name="txtusername" class="txtInput" type="text" placeholder="accord.mod12" required />
      <label for="password">Password</label>
      <input id="password" name="txtpassword" class="txtInput" type="password" placeholder="*******" required />
      <label for="birthdate">Date of birth</label>
      <div>
        <input type="date" name="txtbirthday" />

      </div>
      <input type="submit" id="btnRegister" name="btnRegister" value="Register">
      <br>
      <a href="login.php">Already have an account?</a>
    </form>
  </div>
</body>


</html>

<?php
if (isset($_POST['btnRegister'])) {
  //retrieve data from form and save the value to a variable
  //for tbluserprofile

  //////////////////////////////////
  $email        = $_POST['txtemail'];
  $displayName  = $_POST['txtdisplayname'];
  $uname        = $_POST['txtusername'];
  $bday         = $_POST['txtbirthday'];

  //for tbluseraccount
  $pword        = $_POST['txtpassword'];

  //save data to tbluserprofile			

  //Check tbluseraccount if username is already existing. Save info if false. Prompt msg if true.
  $sql2 = "SELECT * FROM tbluseraccount WHERE username='" . $uname . "'";
  $result = mysqli_query($connection, $sql2);
  $row = mysqli_num_rows($result);
  if ($row == 0) {

    $hashedPW = password_hash($pword, PASSWORD_DEFAULT);

    $sql = "INSERT INTO tbluseraccount(emailadd,username,password,usertype) VALUES('" . $email . "','" . $uname . "','" . $hashedPW . "', 'user')";
    mysqli_query($connection, $sql);

    $sql1 = "INSERT INTO tbluserprofile(gender,birthdate,displayname) VALUES('male' ,'" . $bday . "', '" . $displayName . "')";
    mysqli_query($connection, $sql1);

    showMessage("New Record Saved");
    //header("location: index.php");
  } else {
    showMessage("Account Already Exists");
  }
}

function showMessage($message)
{

  echo "
      <div id='message-box'>
        <p>$message</p>
      </div>
    ";

  echo "
      <script>
        setTimeout(function() {
          var messageBox = document.getElementById('message-box');
          if (messageBox) {
            messageBox.parentNode.removeChild(messageBox);
          }
        }, 3000);
      </script>
    ";
}


?>