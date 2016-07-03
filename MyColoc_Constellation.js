var URI = "";
var CLE = "";
var pseudo = "";
var email = "";
var mdp ="";
var texte = "";



function sleep(miliseconds) {
   var currentTime = new Date().getTime();

   while (currentTime + miliseconds >= new Date().getTime()) {
   }
}
function connexionConstellation(URIc, CLEc)
{
    var constellation = $.signalR.createConstellationConsumer(URIc, CLEc, "Test API");
    constellation.connection.start();    
    var controller = $.signalR.createConstellationController(URIc, CLEc, "TestAPI");
    controller.connection.start();
    return [constellation,controller];
}
function envoyerNote(texteNote)
{
    $.ajax({
            type : 'POST',
            url : 'traitement_constellation.php',
            success : function(data)
            {
                URI = data.URI;
                CLE = data.CLE;
                pseudo = data.pseudo;
                texte = texteNote
            },
            dataType : 'json',
            async : false
    });

        if(URI != "")
        {
            var constell = connexionConstellation(URI,CLE);
            var constellation = constell[0];
            var controller = constell[1];
            //quand la connexion est etablie on envoi le message à constellation
            constellation.connection.start().done(function () {
                console.log("send message");
                constellation.server.sendMessage({ Scope: 'Package', Args: ['MyColoc_Website'] }, 'CreerNote',  [texte,pseudo]);
            });
        }
}
function supprimerNote(texteNote)
{
    $.ajax({
            type : 'POST',
            url : 'traitement_constellation.php',
            success : function(data)
            {
                URI = data.URI;
                CLE = data.CLE;
                pseudo = data.pseudo;
                texte = texteNote
            },
            dataType : 'json',
            async : false
    });

        if(URI != "")
        {
            var constell = connexionConstellation(URI,CLE);
            var constellation = constell[0];
            var controller = constell[1];
            //quand la connexion est etablie on envoi le message à constellation
            constellation.connection.start().done(function () {
                console.log("send message");
                constellation.server.sendMessage({ Scope: 'Package', Args: ['MyColoc_Website'] }, 'SupprimerNote',  [pseudo,texte]);
            });
        }
}
function envoyerCourse(texteCourse)
{
     $.ajax({
            type : 'POST',
            url : 'traitement_constellation.php',
            success : function(data)
            {
                URI = data.URI;
                CLE = data.CLE;
                pseudo = data.pseudo;
                texte = texteCourse
            },
            dataType : 'json',
            async : false
    });

        if(URI != "")
        {
            var constell = connexionConstellation(URI,CLE);
            var constellation = constell[0];
            var controller = constell[1];
            //quand la connexion est etablie on envoi le message à constellation
            constellation.connection.start().done(function () {
                console.log("send message");
                constellation.server.sendMessage({ Scope: 'Package', Args: ['MyColoc_Website'] }, 'CreerCourse',  [texte,pseudo]);
            });
        }
}
function supprimerCourse(texteCourse)
{
     $.ajax({
            type : 'POST',
            url : 'traitement_constellation.php',
            success : function(data)
            {
                URI = data.URI;
                CLE = data.CLE;
                pseudo = data.pseudo;
                texte = texteCourse
            },
            dataType : 'json',
            async : false
    });

        if(URI != "")
        {
            var constell = connexionConstellation(URI,CLE);
            var constellation = constell[0];
            var controller = constell[1];
            //quand la connexion est etablie on envoi le message à constellation
            constellation.connection.start().done(function () {
                console.log("send message");
                constellation.server.sendMessage({ Scope: 'Package', Args: ['MyColoc_Website'] }, 'SupprimerCourse',  [pseudo,texte]);
            });
        }
}
function envoyerEvenement(nom, details, date)
{
     $.ajax({
            type : 'POST',
            url : 'traitement_constellation.php',
            success : function(data)
            {
                URI = data.URI;
                CLE = data.CLE;
                pseudo = data.pseudo;
            },
            dataType : 'json',
            async : false
    });

        if(URI != "")
        {
            var constell = connexionConstellation(URI,CLE);
            var constellation = constell[0];
            var controller = constell[1];
            //quand la connexion est etablie on envoi le message à constellation
            constellation.connection.start().done(function () {
                console.log("send message");
                constellation.server.sendMessage({ Scope: 'Package', Args: ['MyColoc_Website'] }, 'CreerEvenement',  [nom,pseudo,details,date]);
            });
        }  
}
function supprimerEvenement(nom)
{
     $.ajax({
            type : 'POST',
            url : 'traitement_constellation.php',
            success : function(data)
            {
                URI = data.URI;
                CLE = data.CLE;
                pseudo = data.pseudo;
            },
            dataType : 'json',
            async : false
    });

        if(URI != "")
        {
            var constell = connexionConstellation(URI,CLE);
            var constellation = constell[0];
            var controller = constell[1];
            //quand la connexion est etablie on envoi le message à constellation
            constellation.connection.start().done(function () {
                console.log("send message");
                constellation.server.sendMessage({ Scope: 'Package', Args: ['MyColoc_Website'] }, 'SupprimerEvenement',  [nom,pseudo]);
            });
        }  
}
function changerArgent(valeur,type)
{
     $.ajax({
            type : 'POST',
            url : 'traitement_constellation.php',
            success : function(data)
            {
                URI = data.URI;
                CLE = data.CLE;
                pseudo = data.pseudo;
            },
            dataType : 'json',
            async : false
    });

        if(URI != "")
        {
            var constell = connexionConstellation(URI,CLE);
            var constellation = constell[0];
            var controller = constell[1];
            //quand la connexion est etablie on envoi le message à constellation
            constellation.connection.start().done(function () {
                console.log("send message");
                constellation.server.sendMessage({ Scope: 'Package', Args: ['MyColoc_Website'] }, 'ChangerArgent',  [pseudo,valeur,type]);
            });
        }  
}

$(function(){


    $('#formu_ins').on('submit',function(){
        //recuperation des donnes de connexion a la constellation
       
        console.log("On va faire le post");
        URI = $('#URI').val();
        CLE = $('#CLE').val();
        pseudo = $('#pseudo').val();
        mdp = $('#mdp').val();
        email = $('#email').val();
        //connexion
        $.ajax({
            type : 'POST',
            url : 'traitement_inscription.php',
            data : {
                email : $('#email').val(),
                mdp : $('#mdp').val(),
                ddn : $('#ddn').val(),
                pseudo : $('#pseudo').val(),
                URI : $('#URI').val(),
                CLE : $('#CLE').val()
            },
            success : function(data)
            {
                console.log("inscription  ok");
            },
            dataType : 'text',
            async : false
        });
        //si URI alors connexion a constellation
        if(URI != "")
        {
            var constell = connexionConstellation(URI,CLE);
            var constellation = constell[0];
            var controller = constell[1];
            //quand la connexion est etablie on envoi le message à constellation
            constellation.connection.start().done(function () {
                constellation.server.sendMessage({ Scope: 'Package', Args: ['MyColoc_Website'] }, 'NouveauMembre',  [pseudo,email,mdp,0,0]);
            });
        }
    });

   





});

