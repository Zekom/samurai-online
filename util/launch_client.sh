#!/bin/sh
# +++++++++++++++++++++++++++++++++++++++++++++++++++++++++\
# + Ce fichier permet d'ex�cuter l'application cliente sans +
# + aucune configuration manuelle. Il ne peut cependant pas +
# + �tre ex�cut� directement depuis Eclipse. Il faut l'ex�- +
# + cuter depuis l'explorateur Windows.                     +
# \+++++++++++++++++++++++++++++++++++++++++++++++++++++++++/

OPTION='-Djava.library.path=libs/lwjgl/native/linux'
JARFILE='client.jar'
java $OPTION -jar $JARFILE $*
