# Passport Photo Formatter (Java API)

[![Heroku](https://pyheroku-badge.herokuapp.com/?app=formatter-passport&style=flat)](https://formatter-passport.herokuapp.com/)

Spring Boot back-end application for Passport Photo Formatter app. An app inspired by using Photoshop one too many times to modify photos for passports.

Formats a user-upload portrait image into standard 2" x 2" US passport photo size at 300 dpi. User can download formatted and gridded image for printing. Grid image is 4" x 6" at 300dpi. 

(This project is part of the Passport Photo Formatter application. It hosts the API.)

This app uses facial recognition to autodetect faces to crop to.

# Web Version

[![Heroku](https://pyheroku-badge.herokuapp.com/?app=formatter-passport&style=flat)](https://formatter-passport.herokuapp.com/)

(initializing may take a few seconds, but subsequent uploads will not)

for demo, please go to [display](https://github.com/sophieqguan/passport-photo-formatter-React).

# Technologies

- Java 16+

- Spring Boot (2.5.2)

- Apache Commons (1.3.2) (Commons file upload 1.2.1)

- OpenCV (4.0.0)


# Launch (in companionship of front-end display)
React (front-end) part of this project can be found [HERE](https://github.com/sophieqguan/passport-photo-formatter-React).
## Setup OpenCV

- Ensure that class file version is at least 60 (JRE 16) 

- OpenCV official installation guide can be found [here](https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html)

Run Spring Boot application with `mvn spring-boot:run` at root

Run React application ([Passport-Photo-Formatter-React](https://github.com/sophieqguan/passport-photo-formatter-React)) with `npm start` at root


# Features

## Image Resizing

- resize (scale and crop) a user-uploaded image to fit standard 2 inch x 2 inch US passport photo size.

- place resized image on grid for printing

- using OpenCV for facial recognition to automatically resize image with face as center (if no face is detected, the program will crop with center of image as center).

## Place Image On Grid

- place resized and formatted portrait photo onto a 4 inch x 6 inch grid layout for printing. 


# Sources

Image DPI modification function based on [Astalion's Cubemaker](https://github.com/Astalion/CubeMaker)

OpenCV implemented based on Face Detection tutorial from tutorialspoint



