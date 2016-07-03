using Constellation;
using Constellation.Package;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MyColoc_Website
{
    public class Program : PackageBase
    {
        static void Main(string[] args)
        {
            PackageHost.Start<Program>(args);
        }

        public override void OnStart()
        {
            //recuperation des states objects anciennement créé
            PackageHost.LastStateObjectsReceived += (s, e) =>
            {
                foreach (StateObject so in e.StateObjects)
                {
                    PackageHost.PushStateObject(so.Name, so.DynamicValue, so.Type, so.Metadatas, so.Lifetime);
                }
            };
            PackageHost.WriteInfo("Package starting - IsRunning: {0} - IsConnected: {1}", PackageHost.IsRunning, PackageHost.IsConnected);
          
        }

        [MessageCallback]
        public String FindMembre(String pseudo)
        {
            String existe = "non";
            PackageHost.StateObjectUpdated += (s, e) =>
            {
                existe = "non";
            };
            PackageHost.RequestStateObjects(name: "Membre:"+pseudo);
            System.Threading.Thread.Sleep(1000);
            PackageHost.WriteInfo("Membre : " + pseudo + " Existe : " + existe);
            return existe;
        }


        /// <summary>
        /// Supprimer un evenement.
        /// </summary>
        /// <param name="nom"></param>
        /// <param name="createur"></param>
        [MessageCallback]
        public void SupprimerEvenement(String nom, String createur)
        {
            PackageHost.StateObjectUpdated += (s, e) =>
            {
                String nomEvenement = e.StateObject.DynamicValue.nom;
                String createurEvenement = e.StateObject.DynamicValue.createur;
                String nameEvenement = e.StateObject.Name;

                if (nomEvenement == nom && createurEvenement == createur)
                {

                    PackageHost.WriteInfo("Suppresion de l'evenement : " + nameEvenement);
                    PackageHost.PurgeStateObjects(name: nameEvenement);
                }
            };
            PackageHost.RequestStateObjects(type: "MyColoc_Website.Evenement");
        }

        /// <summary>
        /// Suppresion d'une course
        /// </summary>
        /// <param name="pseudo"></param>
        /// <param name="texte"></param>
        [MessageCallback]
        public void SupprimerCourse(String pseudo, String texte)
        {
            PackageHost.StateObjectUpdated += (s, e) =>
            {
                String texteCourse = e.StateObject.DynamicValue.texte;
                String pseudoCourse = e.StateObject.DynamicValue.pseudo;
                String nameCourse = e.StateObject.Name;

                if (texteCourse == texte && pseudoCourse == pseudo)
                {
                    
                    PackageHost.WriteInfo("Suppresion de la note : " + nameCourse);
                    PackageHost.PurgeStateObjects(name: nameCourse);
                }
            };
            PackageHost.RequestStateObjects(type: "MyColoc_Website.Course");
        }


        /// <summary>
        /// Supprimer une note
        /// </summary>
        /// <param name="pseudo"></param>
        /// <param name="texte"></param>
        [MessageCallback]
        public void SupprimerNote(String pseudo, String texte)
        {
            PackageHost.StateObjectUpdated += (s, e) =>
            {
                String texteNote = e.StateObject.DynamicValue.texte;
                String pseudoNote = e.StateObject.DynamicValue.pseudo;
                String nameNote = e.StateObject.Name;

                if(texteNote==texte && pseudoNote == pseudo)
                {
                    PackageHost.WriteInfo("Suppresion de la note : " + nameNote);
                    PackageHost.PurgeStateObjects(name: nameNote);
                }
            };
            PackageHost.RequestStateObjects(type:"MyColoc_Website.Note");
        }

        /// <summary>
        /// Création d'un nouvel évènement.
        /// </summary>
        /// <param name="nom"></param>
        /// <param name="createur"></param>
        /// <param name="details"></param>
        /// <param name="date"></param>
        [MessageCallback]
        public void CreerEvenement(String nom,String createur,String details,String date)
        {
            int nombre = 1;

            PackageHost.StateObjectUpdated += (s, e) =>
            {
                nombre += 1;
                PackageHost.WriteInfo("nombre:" + nombre);
            };
            PackageHost.RequestStateObjects(type: "MyColoc_Website.Evenement");

            System.Threading.Thread.Sleep(1000);
            String name = "Evenement:" + createur + ":" + nombre;
            PackageHost.WriteInfo("Création d'un évènement.");
            PackageHost.PushStateObject<Evenement>(name, new Evenement() { nom = nom, createur = createur, details = details, date = date });
        }


        /// <summary>
        /// Création d'une nouvelle course dans la coloc.
        /// </summary>
        /// <param name="texte"></param>
        /// <param name="pseudo"></param>
        [MessageCallback]
        public void CreerCourse(String texte, String pseudo)
        {
            String date = DateTime.Today.ToString("d");
            int nombre = 1;

            PackageHost.StateObjectUpdated += (s, e) =>
            {
                nombre += 1;
            };
            PackageHost.RequestStateObjects(type: "MyColoc_Website.Course");

            System.Threading.Thread.Sleep(1000);
            String name = "Course:" + pseudo + ":" + nombre;
            PackageHost.WriteInfo("Création d'une course.");
            PackageHost.PushStateObject<Course>(name, new Course() { texte = texte, pseudo = pseudo, date = date });
        }



        /// <summary>
        /// Création d'une note dans la coloc.
        /// </summary>
        /// <param name="texte"></param>
        /// <param name="pseudo"></param>
        [MessageCallback]
        public void CreerNote(String texte, String pseudo)
        {
            int nombre = 1;

            PackageHost.StateObjectUpdated += (s, e) =>
            {
                nombre += 1;
            };
            PackageHost.RequestStateObjects(type:"MyColoc_Website.Note");
                       
            System.Threading.Thread.Sleep(1000);
            String name = "Note:" + pseudo + ":" + nombre;
            PackageHost.WriteInfo("Création d'une note.");
            PackageHost.PushStateObject<Note>(name, new Note() { texte = texte, pseudo = pseudo });
        }


        /// <summary>
        /// Création d'un nouveau membre MyColoc
        /// </summary>
        /// <param name="pseudo"></param>
        /// <param name="Email"></param>
        /// <param name="mdp"></param>
        /// <param name="argent_mise"></param>
        /// <param name="argent_due"></param>
        /// <param name="id_coloc"></param>
        [MessageCallback]
        public void NouveauMembre(String pseudo,String Email,String mdp,int argent_mise,int argent_due)
        {
            PackageHost.WriteInfo("Creation d'un nouveau membre.");
            Membre newMembre = new Membre() { Pseudo = pseudo, Email = Email, mdp = mdp, argent_mise = argent_mise, argent_due = argent_due};
            String name = "Membre:"+pseudo;
            PackageHost.PushStateObject<Membre>(name, newMembre);
        }

 
        /// <summary>
        /// Fonction qui change argent due/mise du membre.
        /// Le type est due ou don
        /// </summary>
        /// <param name="pseudo"></param>
        /// <param name="valeur"></param>
        /// <param name="type"></param>
        [MessageCallback]
        public void ChangerArgent(String pseudo, int valeur, String type)
        {
            // type = due ou don
            PackageHost.WriteInfo("Changement de l'argent du membre");

            //Il faut récupérer le stateobject avec le bon nom
            //modifier l'object
            //le renvoyer
            PackageHost.StateObjectUpdated += (s, e) =>
            {
                    PackageHost.WriteInfo("StateObject membre récupéré.");
                    int maj_argent_mise = e.StateObject.DynamicValue.argent_mise;
                    int maj_argent_due = e.StateObject.DynamicValue.argent_due;                   
                    String newEmail = e.StateObject.DynamicValue.Email;
                    String newMdp = e.StateObject.DynamicValue.mdp;

                    if(type=="due")
                    {
                        maj_argent_due += valeur;
                    }
                    else
                    {
                        maj_argent_mise += valeur;
                    }
                    Membre newMembre = new Membre() {Pseudo = pseudo,Email=newEmail,mdp=newMdp,argent_due=maj_argent_due,argent_mise=maj_argent_mise};
                    String name = "Membre:" + pseudo;
                    PackageHost.PushStateObject<Membre>(name, newMembre);
            };
            PackageHost.RequestStateObjects(name: "Membre:"+pseudo);
            
        }
}





    [StateObject]
    public class Evenement
    {
        public String nom { get; set; }
        public String createur { get; set; }
        public String details { get; set; }
        public String date { get; set; }
    }

    [StateObject]
    public class Course
    {
        public String texte { get; set; }
        public String pseudo { get; set; }
        public String date { get; set; }
    }



    [StateObject]
    public class Note
    {
        public String texte { get; set; }
        public String pseudo { get; set; }
    }

 


    [StateObject]
    public class Membre
    {
        public String Pseudo { get; set; }
        public String Email { get; set; }
        public String mdp { get; set; }
        public int argent_mise { get; set; }
        public int argent_due { get; set; }
    }
}
