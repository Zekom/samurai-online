#!/bin/sh
# +++++++++++++++++++++++++++++++++++++++++++++++++++++++++\
# + Ce fichier permet d'ex�cuter l'application serveur sans +
# + aucune configuration manuelle. Il ne peut cependant pas +
# + �tre ex�cut� directement depuis Eclipse. Il faut l'ex�- +
# + cuter depuis l'explorateur Windows.                     +
# \+++++++++++++++++++++++++++++++++++++++++++++++++++++++++/

JARFILE='serveur.jar'
java -jar $JARFILE $*
