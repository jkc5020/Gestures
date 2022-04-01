# Gestures

## Overview
This app was created to replicate common "Moto Actions" to all Android users. When I replaced my Motorola phone with a new one, I missed their exclusive
features, so I decided to build my own app so actions like a chopping motion would turn on the flashlight or flipping the phone would turn on Do Not Disturb. 

## Components Used
 - FE
    - Common UI components such as switches, sliders, and buttons to process the functionality the user wanted their device to have to monitor sensors. 
    For example, if the user wanted to have both the flashlight and DND checked, or just one or the other. I hope to add more features later on. 
 
 - BE
    - A foreground service was used to monitor the sensors, specifically the gyroscope, gravity, and accelerometer sensors. Several calculations are done for each 
    implemented action to fine tune the sensor readings, so that the actions only execute when they should, and in a predictable manner and not being too sensitive 
    and/or unresponsive. The slider in the UI is specifically used to control how sensitive the flashlight should be.
    
    
##### Note
This was my first Android app, where I learned about the OS and the basics. While the app functions as I intended, I plan on going back and refactoring in the future.
More efficient, cleaner-looking code is found in my DriveTracking repo, which is what I am currently working on. However, that app is a work in progress, while this 
one is mostly complete.
    
