Lege hier eine Datei mit dem Namen sample.pdf ab, damit der "Open local PDF"
Deep-Link funktioniert. Du kannst irgendein PDF nehmen.

Beim ersten Klick auf den Link kopiert die App diese Datei nach
filesDir/documents/sample.pdf und uebergibt sie via FileProvider an einen
PDF-Reader.

Wenn keine sample.pdf da ist, zeigt die App einen Toast mit Anleitung.
