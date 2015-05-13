# Requirement for Pace-keeper #

## 1. 	Music Player module ##

The main function of the music player is to serve as a normal music player and respond to user running pace by getting data from pedometer. There are actually two music player UIs, one is a collapsed interface located at the main UI. Another is a full screen interface activated by clicking the collapsed interface. Music library interface are also included in order to enhance songs organization.

  1. 1	Collapsed interface
    1. Collapsed interface contains an album art picture placed on the left side.
    1. Control panel is at the lower part. It includes basic functionality such as play, pause, previous and skip to next song.
    1. A button called “Pace Override” should be placed just below the collapsed interface. It allows users to choose whether the pedometer should affect music speed or not.
    1. Song information such as name or artist should be loaded from file’s ID3 tags and is aligned to the right of album art.


  1. 2    	Full screen interface
    1. Full screen interface pops up when the collapsed interface is dragged downwards.
    1. A bigger album thumbnail is shown, placing as the same position as the collapsed interface.
    1. Control panel is placed at the original position as collapsed interface.
    1. Music playlist is exposed as the upper part of the full screen interface, allowing users to select songs in an intuitive way
    1. Just above the control panel, a slide bar is presented. Users can search for their favorite part of the music.
    1. Extra functions such as shuffle and repeat options can be toggled through the full screen interface.
    1. Volume is controlled by sliding the album art up or down. Degrees of volume can be adjusted.
    1. When users double tap on the album art picture, a list of music library appears.

  1. 3            	Music library interface
    1. The list shows songs in different way when different button (song name, album, artist) on the top is selected.
    1. For example, when album button is tapped. The songs will be grouped, depending which they belong. When user gets inside a folder, songs are showed in a list view with song content and album art.
    1. The music list can be slide up or down to show more songs.
    1. When a song is pressed, it plays and the screen holds. That is user can only get back to the full screen interface by the button on top.
    1. When a song is tapped and hold, a window pops up and gives different option for users, including deletion, adding to playlist, or details.

  1. 4            Playlist interface
    1. The playlist view shows songs the user is chosen for playing in the music player.
    1. The playlist should be preserved as the music player is aborted, or the application is closed and reopened.
    1. Users are allowed to change the order of songs to play in the playlist.

## 2. 	Pedometer module ##

2.1	User Interface

There are two interfaces for the pedometer: A smaller interface in the main UI,and the full screen interface.

> 2.1.1	The small interface
    1. The small interface of the pedometer should be located at the bottom half of the main UI.
    1. An indicator is shown to prompt user’s current pace, it serves as “Pace Override” button in the music module
    1. A graph of user’s movement is shown for the small interface, by which the graph should be plotted with data collected from accelerometer.
    1. Users can scroll left or right to reveal more information regarding the pedometer, this triggers the transition to the full screen interface.

> 2.1.2	the full screen interface
    1. The full screen interfaces of the pedometer are activated by sliding the small interface in the main UI.
    1. Additional information such as steps taken, speed or distance traveled should be shown for the left side of the main UI.
    1. A graph for the journey’s pace change can be logged and be seen by the interface. Users are expected to see their pace is of slow, normal or fast, with the addition of the relative duration for a particular pace.
    1. Historical records are shown for the right side of the main UI. It includes the past journeys a user encountered, and details that was shown in the left side can be reviewed by tapping on individual records.
    1. Users should also be able to remove particular record by holding and sliding to either of the side. One chance is given to undo the remove process

2.2	Pace detection
> 2.2.1	Sensor utilization
    1. Accelerometer should only be used to provide data of user steps. This maximizes the compatibility of devices to utilize this main functionality
    1. Additional sensors can be utilized to provide useful information. However, they must not be a mandatory for the application to run
    1. Sensor will always be initialized when entering the main UI unless it is explicitly disabled

> 2.2.2	Pace algorithm
    1. Pace algorithm would interfere with music player only when sensor detects a rhythmic motion
    1. To ensure accurate detection, all data gathered from accelerometer should be used to estimate the steps taken. The represented variable could be as G-force or others kinds of values
    1. A step is counted by considering the slope of a variable exceeding certain threshold value over a fixed time period.
    1. Additional check can be added to improvise the optimal threshold value to allow accurate step detection. Examples includes an ever-changing threshold based on bias of the G-force value. This lessens the variance induced by background noise or unwanted motion
    1. Relative pace can be calculated using the duration between footsteps. Algorithms such as averaging weight or sample mean could be adopted to estimate the most acceptable speed of the user, thus generating the threshold values for slow/fast pace that adjust the music speed
    1. Abnormal activities should also be detected, such as user stopped taking steps temporarily. As such events might not be user’s intention, those data should be discarded to minimize the effect to other information

2.3	Settings
> 2.3.1	Calibration
    1. A simple instruction should be brought to user to guide them through the calibration process
    1. Users should be prompted for demonstrating pace upon different situations. The situations could be walking, jogging and running.
    1. Each detection is done automatically as users do their motion. Another prompt such as vibration should be presented when the detection process is finished
    1. After the calibration process, the values should be stored as profiles and users are allowed to fine-tune their profiles
> 2.3.2	Personal Profiles
    1. All personal profiles should be stored in local device and should be easily-manageable by users
    1. Users can create their profiles by modifying from the default profile, or editing the user-generated profile from the calibration process
    1. Users are also allowed to tag their profiles with customizing the color of profile, and delete the existing profiles at will

## 3. 	Goals and achievements module ##

3.1	Routine Selection
  1. Users can start the Goal module on the initial page
  1. The list of routines is shown, allowing users to select their favourite routine
  1. After selecting the routine, users are redirected to the main UI. The “Pace Override” button should be acted as pausing and continuing the routine

3.2	Achievements

Achievement is an awarding system in purpose of enhancing user’s motivation. For specific target that user has achieved, system will give user a credit or virtual trophy for corresponding achievements

> 3.2.1	Personal Achievement
    1. A pie chart showing the total progress of personal achievement should be shown at the upper part of the achievement page
    1. Recent achievements should be shown at the lower half of the page
    1. There should also be a button to show the list of achievements that a user can accomplish, with an indicator showing the difficulty
    1. For finished achievement, users can see the related record which accomplishes it
> 3.2.2	Joint Achievement
    1. A pie chart showing the total progress of joint achievement should be shown at the upper part of the page
    1. Lower part shows the achievements with the percentage of personal contribution
    1. For a specific joint achievement, the top contributor would be shown as a list, as well as their respective information regarding the achievement
    1. Synchronization should be done under certain interval, but not all the time to minimize the impact to achievement system server

## 4.	Statistics ##

Statistics shows the summary of users regarding the usage of the application. The data is collected from the previous records of the application.

  1. Aggregated information such as total number of steps, miles travelled, etc, is shown below.
  1. Other information can be estimated with given data such as calorie burnt with weight information.
  1. Some app-related statistics like how frequent the user uses the application ,or how long the user use each day will be included

## 5.	Sharing ##

The application allows users to share their performance and make a recommendation of the application towards their friends via popular social platforms such as Facebook or Twitter. They can share what they have achieved. Event can also be made while accessing the goals to motivate multiple users to join.

## 6.	Miscellaneous ##

6.1	General Settings
> General Settings can be opened in the initial page of the application. It activates a new page that contains several options to allow user tweak different aspects of the app

> 6.1.1	Clear History
> > Clear history will give user an opportunity to clear the data of their running history.


> 6.1.2	Reset Application
> > Reset Data provide user an opportunity to clear all the data in the app including history,statistic,connections and every other data,it allows users to reset their app like when they first use it.