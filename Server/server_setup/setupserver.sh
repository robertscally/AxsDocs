sudo apt-get update
sudo apt-get --yes --force-yes install tesseract-ocr
sudo apt-get --yes --force-yes install imagemagick
sudo apt-get --yes --force-yes install apache2
sudo apt-get --yes --force-yes install php5
sudo apt-get --yes --force-yes install php5-pspell
sudo /etc/init.d/apache2 restart
sudo rm /etc/php5/apache2/php.ini
sudo cp php.ini /etc/php5/apache2/
sudo /etc/init.d/apache2 restart
sudo chmod -R 777 /var/www
sudo cp -R ocrserver/ /var/www/
sudo chmod -R 777 /var/www
