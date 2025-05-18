package ex2;

public class Enunciat {
/*
    1. Copia la classe HashTable.java del package "original" a "ex2" i munta-hi un joc de proves per als mètodes "put",
       "get" , "drop" , "count" i "size". Dins els jocs de proves pots fer servir el mètode "toString" que proporciona
       la taula de hash per a obtenir una cadena de text que representa el seu contingut, el mètode "toString" funciona
       correctament i està lliure d'errors.

    2. Mira al final d'aquest document, trobaràs un llistat de tot el que cal provar per trobar els errors que té el
       el codi font. Dins un mateix joc de proves, pots fer servir combinacions de mètodes (per exemple "put" i "get").
       El llistat del final indica qué es allò que cal provar, però la forma de fer-ho (molts jocs de proces curts, pocs
       jocs de proves molt llargs, en quin order, etc.) l'has de decidir tu i ha de ser diferent a la que presentin els
       teus companys.

    3. SOLUCIONA els errors que hi trobis al codi font de la taula de hash però NO ESBORRIS CODI, tan sols comenta'l
       perquè es pugui veure el codi antic i el codi nou.

    4. NO HI APLIQUIS REFACCIÓ!!!

    5. Grava en vídeo el següent:
       5.1. Explica què són les proves unitàries i perquè serveixen.
       5.2. Mostra els teus jocs de proves i explica com els has dissenyat, què pretèns demostrar o comprovar, perquè
            l'has fet d'aquesta manera i no d'una altra, etc.
       5.3. Mostra com has sol·lucionat els errors, explicant quin era el problema i com ho has solucionat.
       5.4. Explica el concepte de TDD i argumenta si s'ha seguit al llarg d'aquesta pràctica o no.
       5.5. Recorda que cal fer la demostració del funcionament de les proves dins de GitHub si encara no ho has fet.

    IMPORTANT:
        - L'exercici no puntua si dins el vídeo no queda argumentat el treball realitzat.
        - A continuació s'explica el funcionament de la taula de hash.
        - Al final del tot trobaràs un llistat de totes les coses que han de comprovar els teus jocs de proves.

-----------------------------------------------------------------------------------------------------------------------

### COM FUNCIONA UNA TAULA DE HASH ###

    A continuació s'explica el funcionament de la implementació de taula de hash proposada:

    Una taula de hash representa un magatzem de valors ordenats (similar a un array) on els elements es
    componen d'una clau i un valor.

        [0] -> <clau, valor>
        [1] -> <clau, valor>
         .
         .
         .
        [n] -> <clau, valor>

    Quan es vol afegir un element a la taula de hash, s'indica quina és la clau del valor a afegir, per exemple:
    si es vol afegir un objecte de tipus Persona a la taula, s'indicarà la clau de la Persona (el seu ID) i la mateixa
    persona ->; hashtable.put(persona.ID, persona)

    La taula de hash col·locarà a aquesta persona en una posició fixa calculada, per exemple la posició 32. Quan un
    programador vulgui recuperar la persona, necessitarà saber-ne el ID perquè la taula de hash la pugui retornar:
    per exemple ->; hastable.get(persona.ID)

    És a dir, es pot veure una taula de hash com un array, on trobar un element es fa a l'instant en comptes de fer
    cerques seqüencials.

    Problema: les col·lisions. Com s'ha comentat, la taula de hash calcula la posició a assignar aplicant un algoritme
    sobre el ID, però això vol dir que diferents IDs poden resultar en la mateixa posició. La forma de solucionar-ho és
    convertir les posicions de la hash table en llistes enllaçades tal que així:

        [0] -> 	<clau, valor>
                <clau, valor>
                <clau, valor>

        [1] ->  <clau, valor>
         .		<clau, valor>
         .
         .
        [n] ->  <clau, valor>
                <clau, valor>

    Com es pot veure a l'exemple anterior, hi ha 3 elements que corresponen a la posició 0, 2 per a la 1, etc.
    Amb aquest canvi, l'accés es converteix en semi-directe, ja que la posició a la llista interna si és directa,
    però l'element s'ha de buscar seqüencialment dins aquesta llista. Tingues en compte que el ID ha de ser únic, per
    tant dos elements diferents amb el mateix ID s'estaran sobreescrivint l'un sobre l'altre.

    Exemple amb valor simulats:

        //la taula de hash es buida ->				[]

        ht.put(99, p1);				->				[0]
                                                    [1]
                                                     .
                                                     .
                                                     .
                                                    [9] ->	<99, p1>
                                                     .
                                                     .
                                                     .
                                                    [n]


        ht.put(21, p2)				->				[0]
                                                    [1] -> 	<21, p2>
                                                     .
                                                     .
                                                     .
                                                    [9] ->	<99, p1>
                                                     .
                                                     .
                                                     .
                                                    [n]

        ht.put(2, p3)				->				[0]
                                                    [1] -> 	<21, p2>
                                                     .
                                                     .
                                                     .
                                                    [9] ->	<99, p1>
                                                     .		<02, p3>
                                                     .
                                                     .
                                                    [n]

        ht.put(2, p4)				->				[0]
                                                    [1] -> 	<21, p2>
                                                     .
                                                     .
                                                     .
                                                    [9] ->	<99, p1>
                                                     .		<02, p4>
                                                     .
                                                     .
                                                    [n]

-----------------------------------------------------------------------------------------------------------------------

### LES PROVES QUE CAL FER ###

    A continuació es detalla tot el que cal comprovar dins els jocs de proves, recorda que això és un llistat del que cal
    comprovar, però la forma de fer-ho depèn de tu.

    Put:
        S'ha de fer servir el mètode "toString" (aquest mètode no falla) per a comprovar que la taula conté els elements correctes
        després de fer servir "put".

        S'ha de fer servir el mètode "put" generar les següents situacions:
            Inserir un element que no col·lisiona dins una taula vuida (sense elements).
            Inserir un element que no col·lisiona dins una taula no vuida (amb elements).
            Inserir un element que col·lisiona dins una taula no vuida, que es col·locarà en 2a posició dins el mateix bucket.
            Inserir un element que col·lisiona dins una taula no vuida, que es col·locarà en 3a posició dins el mateix bucket.
            Inserir un elements que ja existeix (update) sobre un element que no col·lisiona dins una taula no vuida.
            Inserir un elements que ja existeix (update) sobre un element que si col·lisiona (1a posició) dins una taula no vuida.
            Inserir un elements que ja existeix (update) sobre un element que si col·lisiona (2a posició) dins una taula no vuida.
            Inserir un elements que ja existeix (update) sobre un element que si col·lisiona (3a posició) dins una taula no vuida.

    Get:
        S'ha de fer servir el mètode "get" per a comprovar que la taula retorna els elements correctes.

        S'ha de fer servir el mètode "put" generar les següents situacions:
            Obtenir un element que no col·lisiona dins una taula vuida.
            Obtenir un element que col·lisiona dins una taula (1a posició dins el mateix bucket).
            Obtenir un element que col·lisiona dins una taula (2a posició dins el mateix bucket).
            Obtenir un element que col·lisiona dins una taula (3a posició dins el mateix bucket).
            Obtenir un elements que no existeix perquè la seva posició està buida (no hi ha cap element dins el bucket).
            Obtenir un elements que no existeix, tot i que la seva posició està ocupada per un altre que no col·lisiona.
            Obtenir un elements que no existeix, tot i que la seva posició està ocupada per 3 elements col·lisionats.

     Drop:
        S'ha de fer servir el mètode "toString" per a comprovar que la taula conté els elements correctes després de
        fer servir "drop".

        S'ha de crear una taula que conté un element que no col·lisiona i altres 3 elements que si col·lisionen entre ells,
        tal i com s'ha fet a les proves anteriors.

        S'ha de fer servir el mètode "drop" generar les següents situacions:
            Esborrar un element que no col·lisiona dins una taula.
            Esborrar un element que si col·lisiona dins una taula (1a posició dins el mateix bucket).
            Esborrar un element que si col·lisiona dins una taula (2a posició dins el mateix bucket).
            Esborrar un element que si col·lisiona dins una taula (3a posició dins el mateix bucket).
            Eliminar un elements que no existeix perquè la seva posició està buida (no hi ha cap element dins el bucket).
            Eliminar un elements que no existeix, tot i que la seva posició està ocupada per un altre que no col·lisiona.
            Eliminar un elements que no existeix, tot i que la seva posició està ocupada per 3 elements col·lisionats.

      Count:
        S'ha de repetir tot el que s'ha fet amb "put" i comprovar amb "count" que el número de nodes és correcte.
        S'ha de repetir tot el que s'ha fet amb "drop" i comprovar amb "count" que el número de nodes és correcte.

      Size:
        S'ha de repetir tot el que s'ha fet amb "put" i comprovar amb "size" que el tamany de la taula és correcte.
        S'ha de repetir tot el que s'ha fet amb "drop" i comprovar amb "size" que el tamany de la taula és correcte.

*/
}
