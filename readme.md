# Galactic (spaceship game for android)
Spaceship Game for Android is a 2D roguelike game written in Java. Players explore the universe in a customizable ship, going from solar system to solar system,
exploring planets, defeating enemies and getting loot. The game features on-the-fly map generation, different exploration events and proceedurally
generated items for the player. 
An early demo is available on the Google play store, see instructions below.

## Installing
### Method 1: Using an android device:
If you have an android 6.0 device (or newer) available, it is reccomened you download the app through the google play store. 
either:
* Search for 'Edministrator'
* Download from this url: https://play.google.com/store/apps/details?id=uk.co.edministrator.spaceshipgame

Alternatively, the APK can be installed with an APK manager from this url:
http://edministrator.co.uk/galactic.apk

#### Method 2: Through emulation:
Alternatively, the source code can be downloaded and run through an emulator or through Android studio.
#### For Android studio:
* Download the git repository
* Extract it somewhere safe
* Install Android Studio
* Install Java 8 SDK
* Install Device emulator, Suggested Pixel 2 running Android 9.0. 
* Emulation installation instructions here:
* Open the project in android studio
* Run the game (through emulator) from class 'MainActivity'

### Minimum Specification:
* Android 6.0 (Marshmellow) enabled device
* 1GB RAM
* 128MB free storage

### Reccomended Specification:
* Android 7.0 (Nougat) or higher enabled device
* 2GB RAM
* 250MB free storage

### Running Tests
step 1: Download the APK file from the above link.
step 2: log into firebase (firebase.google.com),go to console, make a new project and go to test labs.
step 3: run a 'Game loop' test
step 4: add the APK file, leave scenarios / labels blank
step 5: select the devices you want to test on
step 6: wait about 15mins, then check the results! 


### Changelog: 

2019-04-24:
version 1.2: final version
* Reworked how data is stored in all areas of the game
* Increased player weapon slots to 6/7 from 2/3
* Players now start with three weapons
* Enemies have double weapon capacity
* Enemies now generate a random number of weapons (based on current difficulty)
* Explore/inventory buttons reworked: now uses Android IMG buttons
* Damage numbers now display if the hit was strong/weak
* Added ship stats on selection screen
* deleted redunant code
* Fixed inventory screen crashes, intent overload crashes
* Fixed a crash when pressing the back button on combat

2019-04-10
version 1.1.5: perfomance improvements
* Improved end of  combat screen graphic
* fixed a memory leak
* Fixed multiple crashes
* massive improvements to 6.0 + 7.0 performance.

2019-03-30
version 1.1: combat improvements v2
* added a difficulty modifer - increases rewards / enemy strength.
* updated background image
* updated gun invetory graphics
* added new Explore / inventory buttons
* changed how 'rooms' work again, now active abilities: EMP bomb, Heal, Area heal, Increase fire rate
* Rebalanced ship hp values / number of weapons

2019-03-08: Release version!
version 1.0: Ship selection + general improvements
* Players can now select one of three ships at the start of the game
* combat now has floating damage numbers
* guns now have sound effects
* new music tracks
* better end of combat screens.


2019-02-23
version 0.8: Inventory v1
* Added equipment view.
* Allows for equiping and unequiping of items.
* shows weapon stats (damage, type) on left hand side. 
* saves data when closed.

2019-02-12
version 0.7: Combat improvements 
* added Two new guns; gatling gun and Thermal Cannon
* added other playable ships, though cannot change them in game
* added crit chance, hit chance

2019-02-06
version 0.6: Ship improvements 2
* Added more enemy ships
* Redisgned the concept of rooms, 
* Bridge, Hull and engines are now implemented as 'parts'
* other rooms (engineering center) now hidden passives

2019-02-01
version 0.5: Ship improvements 
* recoded all ship classes (more inheritance!)
* Ships now have a variety of stats
* Created engineering centre, gives passive healing.
* Bitmaps now scale correctly
* Rooms now scale in conjunction with ship

2019-01-28
version 0.4.1:
* Fixed movement bug
* Added git ignore file.
* Cleaned up repo

2019-01-20
Version 0.4 : visual improvements
* Visual improments to Solar system 
* Added Animation when moving from point to point. 
* Adjusted health values

2018-12-9:
Version 0.3.3: bugfixing and Minimum viable product state:
* Combat ends and returns to solar system.
* Activites changed to singleTask (was single instance)
* New items are visible, though not yet interactable.
* This version will be the one demonstrated on oral exam day (Monday 10th dec.).
0.3.2 Inventory system update:
* Fixed palyer not being able to shoot bug
* Combat now ends properly
* Player rewarded item at end of combat, though does not appear in inventory.

2018-12-02
0.3.1 Inventory first version
* new Invetory system, holding crew and weapons
* updated some bitmap sizes
* Frigate now inherits most of its properties from abstract class ship
** this will help with expanding past one ship type

2018-11-20
0.3.0 Art assets + finishing up combat
* updated art assets for ships and projectiles. 
* updated art assets for planets in solar system.
* now able to go from galaxy -> solar system -> combat.
* fixed incorrect rotation on enemy bullets. 

2018-11-19
0.2.3 Real time combat update:
* Enemy now shoots at the player, randomly selecting weapons and rooms.
* added the ability to pan the screen, so each 'side' has either the player or enemy.

2018-11-17
0.2.2 Real time combat:
* Combat system Changed from turn based to real time.
* Ships will now be static objects that cannot move
* Players have multiple weapons to shoot at multiple enemy rooms (areas)
* Ships have different classes and types, currently only a Frigate is implemented.
* Added art assets for UI, ships and background.

2018-11-14
0.2.1 Combat system first version.
* New combat activity
* Able to move square around empty plane using LERP function. 
* Player ship and enemy ship implemented, enemy ship moves randomly.
* Both ships currently move at the same time.

2018-11-11
0.2.0 Solar System map complete.
* Solar System map now generating in a similiar fashion to the galaxy, however will only have one child.
* Solar map is Cyclic (can go from last node to first).
* Galaxy map and solar system map now have art assets.
* updated player art

2018-11-10
0.1.1 Attempted serialization. (save games)
* Added functionality to serialize maps using GSON.
* Loading map from file causes crash (feature disabled for mow)

2018-11-06
0.1.0 Galaxy map complete:
* Map will no longer generate new nodes over existing ones.
* Map nodes will no longer generate "dead ends"
* Created second activity to launch solar system (next map layer)
* Solar system created, still in early development. 

2018-11-01
0.0.3 Map generation:
* Galaxy map now proceedurally generates (with some problems)
* Added sound effects 

2018-10-31
0.0.2 Map Implementation:
* Traversable map, player able to move from point to point.

2018-10-28
0.0.1 First Development build:
Features:
* Rendering of player object and a background image
* Ability to move Player object using touch controls
* Background music.
