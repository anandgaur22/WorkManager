# WorkManager

## What is WorkManager?
WorkManager is a Jetpack library designed to execute asynchronous background tasks. It is best suited for long-running tasks such as:
- Database backup
- File uploading
- API syncing

WorkManager ensures task completion even if the app is closed or the device restarts.

## Why Use WorkManager?
  1.  **Guaranteed Execution** – Ensures task completion even after app closure or device reboot.  
  2.  **Battery Optimized** – Complies with Android’s Doze Mode and background restrictions.  
  3.  **Constraint Support** – Allows setting conditions like network availability, charging state, and storage conditions before executing a task.  
  4.  **Automatic Retry & Scheduling** – Automatically retries failed tasks.  
  5.  **Backward Compatibility** – Supports API Level 14+.

---

## What is Doze Mode?
Doze Mode is a battery optimization feature introduced in Android 6.0 (Marshmallow) to limit background activities when the device is idle (e.g., screen off and not in active use).

### How Does Doze Mode Work?
When the device remains idle for an extended period (e.g., overnight), Android restricts background tasks to save battery.

### Stages of Doze Mode:
1. **Initial Idle Mode**  
   - When the screen turns off, background services continue running for a short time.
2. **Light Doze Mode**  
   - Android restricts background tasks, alarms, and network access.
   - High-priority notifications and essential services are allowed.
3. **Deep Doze Mode**  
   - Network access, background sync, alarms, and wake locks are disabled.
   - Only occasional maintenance windows allow background task execution.
   - Doze Mode remains active until the user unlocks the phone or connects it to a charger.

---

## WorkManager Constraints
Sometimes, background tasks should run only when specific conditions are met. WorkManager allows setting constraints to control task execution.

### Types of Constraints:
1. **Network Constraint** – Task runs only when the internet is available.
2. **Charging Constraint** – Task runs only when the device is charging.
3. **Battery Not Low Constraint** – Task does not run if the battery is low.
4. **Device Idle Constraint** – Task runs only when the device is idle.
5. **Storage Not Low Constraint** – Task does not run if device storage is low.

---

## Types of Work Requests in WorkManager

1. **OneTime Work Request**  
   - Executes only once.
2. **Periodic Work Request**  
   - Repeats at regular intervals.  
   - **Note:** Minimum interval must be 15 minutes.

---

## Conclusion
WorkManager is a powerful tool for scheduling background tasks efficiently while respecting system constraints and battery optimizations. By leveraging WorkManager, developers can ensure reliable task execution with minimal impact on device performance.
