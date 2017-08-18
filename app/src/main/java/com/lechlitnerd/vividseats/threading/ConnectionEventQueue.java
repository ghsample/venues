package com.lechlitnerd.vividseats.threading;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectionEventQueue
{
	private static List<AsyncTask<String, Void, String>> sTasks = new ArrayList<AsyncTask<String, Void, String>>();
	private static Timer sTimer;
	private static final int MAX_CONNECTIONS = 1;
	private static final int FREQUENCY = 50;
	public static final int PRIORITY_HIGH = 1;
	public static final int PRIORITY_LOW = 2;

	/**
	 * public static void queueEvent(AsyncTask<String, Void, String> task)
	 * 
	 * Adds an Aysnc task to a List (queue) and executes the task based on the
	 * MAX_CONNECTIONS allowed at a time. A Timer is scheduled at a rate
	 * specified by FREQUENCY and will continue running until the task list is
	 * empty.
	 * 
	 * @param task
	 */
	public static void queueEvent(AsyncTask<String, Void, String> task)
	{
		sTasks.add(task);
		if (sTasks.size() < MAX_CONNECTIONS)
			task.execute(new String());

		if (sTimer == null)
		{
			sTimer = new Timer();
			EventQueueTask eventQueueTask = new EventQueueTask();
			sTimer.scheduleAtFixedRate(eventQueueTask, 0, FREQUENCY);
		}
	}

	/**
	 * public static void queueEvent(AsyncTask<String, Void, String> task, int
	 * priority)
	 * 
	 * Adds an Aysnc task to a List (queue) and executes the task based on the
	 * MAX_CONNECTIONS allowed at a time. A Timer is scheduled at a rate
	 * specified by FREQUENCY and will continue running until the task list is
	 * empty. In addition, this method contains a variable called priority which
	 * if PRIORITY_HIGH is specified, it will place the task on the top of the
	 * list to be executed as the next available task.
	 * 
	 * @param task
	 * @param priority
	 */
	public static void queueEvent(AsyncTask<String, Void, String> task, int priority)
	{
		if (priority == PRIORITY_HIGH)
			sTasks.add(0, task);
		else if (priority == PRIORITY_LOW)
			sTasks.add(task);

		if (sTasks.size() < MAX_CONNECTIONS)
			task.execute(new String());

		if (sTimer == null)
		{
			sTimer = new Timer();
			EventQueueTask eventQueueTask = new EventQueueTask();
			sTimer.scheduleAtFixedRate(eventQueueTask, 0, FREQUENCY);
		}
	}

	/**
	 * private static class EventQueueTask extends TimerTask
	 * 
	 * Inner class that executes tasks based on a List. This is triggered by a
	 * timer and will continue to run until it detects no tasks are left.
	 * 
	 */
	private static class EventQueueTask extends TimerTask
	{
		/**
		 * public void run()
		 * 
		 * Executes tasks in a list. Shuts off the running Timer if no more
		 * tasks are left to execute.
		 * 
		 */
		@Override
		public void run()
		{
			int runningCount = 0;
			for (int a = 0; a < sTasks.size(); a++)
			{
				AsyncTask<String, Void, String> task = sTasks.get(a);
				if (task.getStatus() == AsyncTask.Status.FINISHED)
				{
					sTasks.remove(a);
					a--;
				}
				else if (task.getStatus() == AsyncTask.Status.RUNNING)
				{
					runningCount++;
				}
			}

			for (int a = 0; a < sTasks.size() && runningCount < MAX_CONNECTIONS; a++)
			{
				AsyncTask<String, Void, String> task = sTasks.get(a);
				if (task.getStatus() != AsyncTask.Status.RUNNING)
				{
					task.execute(new String());
					runningCount++;
				}
			}

			if (sTasks.size() == 0)
			{
				sTimer.cancel();
				sTimer = null;
			}
		}
	}
}
