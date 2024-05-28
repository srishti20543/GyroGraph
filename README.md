
### ACCELEROMETER 

This Android application consists of two main activities: `MainActivity` and `GraphActivity`. The purpose of the app is to collect real-time orientation data from the smartphone's accelerometers, display the orientation angles, and store the data in a database for later analysis. Additionally, the app allows users to view the history of orientation data through graphical representations.

#### MainActivity

- **Description**: The `MainActivity` collects real-time orientation data from the smartphone's accelerometers and displays the pitch, roll, and yaw angles on the screen. It also provides a button to navigate to the `GraphActivity` to view the historical data.
  
- **Functionality**:
  - Collects accelerometer data for pitch, roll, and yaw angles.
  - Displays the real-time orientation angles on the screen.
  - Saves the orientation data into a database for later analysis.
  - Provides a button to navigate to the `GraphActivity` to view historical orientation data.
  - Sends list of angles to other activity to store
  
#### GraphActivity

- **Description**: The `GraphActivity` displays historical orientation data stored in the database as graphs. It provides visual representations of pitch, roll, and yaw angles over time.

- **Functionality**:
  - Retrieves historical orientation data from the database.
  - Generates graphs for pitch, roll, and yaw angles.
  - Allows users to view the historical orientation data in graphical format.
  - Provides a button to navigate back to the `MainActivity`.
  - Add data to database (seconds, x, y, z orientations)

#### Database

- **Description**: The app utilizes a database to store historical orientation data. It includes a schema to define the structure of the database tables.

- **Functionality**:
  - Stores orientation data entries with timestamp, pitch, roll, and yaw angles.
  - Allows retrieval of historical orientation data for visualization.
  - Ensures data persistence across app sessions.

#### Additional Features

- **Export Data**: Users can export orientation data from the app to external sources for further analysis.

- **Prediction and Plotting**: The app supports the prediction and plotting of orientation data for future analysis.

- **Change of Sensing Intervals**: Users can adjust the sensing intervals for data collection to suit their preferences or requirements.

- **Time Series prediction using LSTM**: for last 15-20 seconds based on earlier 100 seconds

#### Usage

1. Launch the app on an Android device.
2. The `MainActivity` will display real-time orientation angles.
3. Navigate to the `GraphActivity` to view historical orientation data in graphs.
4. Explore additional features such as data export, prediction, and adjusting sensing intervals.
