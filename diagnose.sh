#!/bin/bash
echo "=== DIAGNOSTIC COMPLET ==="
echo ""
echo "1. Structure du projet:"
pwd
echo "Fichiers Java:"
find . -name "*.java" -type f | sort
echo ""
echo "2. Contenu de pom.xml (premières 20 lignes):"
head -20 pom.xml
echo ""
echo "3. Test compilation:"
mvn clean compile -q 2>&1 | tail -20
echo ""
echo "4. Test connexion DB:"
timeout 5 psql -U mini_football_db_manager -d mini_football_db -c "SELECT COUNT(*) FROM team;" 2>&1 || echo "Échec connexion"
echo ""
echo "5. Classes compilées:"
ls -la target/classes/ 2>/dev/null || echo "Pas de target/classes/"
