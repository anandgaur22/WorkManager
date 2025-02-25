package com.example.workmanager

/*


What is WorkManager?
WorkManager is a Jetpack library designed to execute asynchronous background tasks.
It is best suited for long-running tasks such as database backup, file uploading, and
API syncing, where guaranteed execution is required.

Why Use WorkManager?

Guaranteed Execution – Ensures task completion even if the app is closed or the device restarts.
Battery Optimized – Complies with Android’s Doze Mode and background restrictions.
Constraint Support – Allows setting conditions like network availability, charging state, and storage
conditions before executing a task.
Automatic Retry & Scheduling – Automatically retries failed tasks.
Backward Compatibility – Supports API Level 14+.


What is Doze Mode?
Doze Mode is a battery optimization feature introduced in Android 6.0 (Marshmallow) to limit
background activities when the device is idle (e.g., screen off and not in active use).

How Does Doze Mode Work?
When the device remains idle for an extended period (e.g., overnight), Android restricts
background tasks to save battery.

Stages of Doze Mode:
Initial Idle Mode:
When the screen turns off, background services continue running for a short time.

Light Doze Mode:
Android restricts background tasks, alarms, and network access, but allows high-priority
notifications and essential services.

Deep Doze Mode:
Network access, background sync, alarms, and wake locks are disabled.
Only occasional maintenance windows allow execution of background tasks.
Doze Mode remains active until the user unlocks the phone or connects it to a charger.


WorkManager Constraints

Sometimes, background tasks should run only when specific conditions are met.
WorkManager allows setting constraints to control task execution.

Types of Constraints:
1. Network Constraint – Task runs only when the internet is available.
2. Charging Constraint – Task runs only when the device is charging.
3. Battery Not Low Constraint – Task does not run if the battery is low.
4. Device Idle Constraint – Task runs only when the device is idle.
5. Storage Not Low Constraint – Task does not run if device storage is low.


Types of Work Requests in WorkManager

1. OneTime Work Request : Executes only once.
2. Periodic Work Request: Repeats at regular intervals.
   Minimum interval must be 15 minutes.



* */