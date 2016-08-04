package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by krish_000 on 8/4/2016.
 */
public class ColorSensorTest extends PushBotTelemetry{

    @Override
    public void start ()

    {
        //
        // Call the PushBotHardware (super/base class) start method.
        //
        super.start ();

        //
        // Reset the motor encoders on the drive wheels.
        //
        reset_drive_encoders ();

    } // start

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Implement a state machine that controls the robot during auto-operation.
     * The state machine uses a class member and encoder input to transition
     * between states.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()

    {
        //----------------------------------------------------------------------
        //
        // State: Initialize (i.e. state_0).
        //
        switch (v_state)
        {
            //
            // Synchronize the state machine and hardware.
            //
            case 0:
                //
                // Reset the encoders to ensure they are at a known good value.
                //
                reset_drive_encoders ();

                //
                // Transition to the next state when this method is called again.
                //
                v_state++;

                break;
            //
            // Drive forward until the encoders exceed the specified values.
            //
            case 1:
                //
                // Tell the system that motor encoders will be used.  This call MUST
                // be in this state and NOT the previous or the encoders will not
                // work.  It doesn't need to be in subsequent states.
                //
                //run_using_encoders ();

                v_sensor_color.enableLed(true);

                //
                // Start the drive wheel motors at full power.
                //

                //set_drive_power (0.20f,0.20f);
                //
                // Have the motor wheels turned the required amount?
                //
                // If they haven't, then the op-mode remains in this state (i.e this
                // block will be executed the next time this method is called).
                //

                if ((v_sensor_color.red() > v_sensor_color.blue())&&(v_sensor_color.red() > v_sensor_color.green()))
                {

                    cdi.setLED(1, true);
                    cdi.setLED(0, false);

                    int red = v_sensor_color.red();
                    int blue = v_sensor_color.blue();
                    int green = v_sensor_color.green();

                    telemetry.addData("01", "red " + red + "true");
                    telemetry.addData("02", "blue " + blue);
                    telemetry.addData("03", "green " + green);

                    v_state++;
                }

                else if ((v_sensor_color.blue() > v_sensor_color.red())&&(v_sensor_color.blue() > v_sensor_color.green()))
                {

                    cdi.setLED(0, true);
                    cdi.setLED(1, false);

                    int red = v_sensor_color.red();
                    int blue = v_sensor_color.blue();
                    int green = v_sensor_color.green();

                    telemetry.addData("01", "red " + red);
                    telemetry.addData("02", "blue " + blue + "true");
                    telemetry.addData("03", "green " + green);

                    v_state++;
                }

                else
                {

                    cdi.setLED(1, true);
                    cdi.setLED(0, true);

                    int red = v_sensor_color.red();
                    int blue = v_sensor_color.blue();
                    int green = v_sensor_color.green();

                    telemetry.addData("01", "red " + red);
                    telemetry.addData("02", "blue " + blue);
                    telemetry.addData("03", "green " + green);
                    v_state++;
                }

                break;
            //
            // Wait...
            //
            case 2:
                if (have_drive_encoders_reset ())
                {
                    v_state= 1;
                }
                break;
            //
            // Turn left until the encoders exceed the specified values.
            //
    /*case 3:
        run_using_encoders ();
        set_drive_power (-1.0f, 1.0f);
        if (have_drive_encoders_reached (2880, 2880))
        {
            reset_drive_encoders ();
            set_drive_power (0.0f, 0.0f);
            v_state++;
        }
        break;
    //
    // Wait...
    //
    case 4:
        if (have_drive_encoders_reset ())
        {
            v_state++;
        }
        break;
    //
    // Turn right until the encoders exceed the specified values.
    //
    case 5:
        run_using_encoders ();
        set_drive_power (1.0f, -1.0f);
        if (have_drive_encoders_reached (2880, 2880))
        {
            reset_drive_encoders ();
            set_drive_power (0.0f, 0.0f);
            v_state++;
        }
        break;
    //
    // Wait...
    //
    case 6:
        if (have_drive_encoders_reset ())
        {
            v_state++;
        }
        break;
    //
    // Perform no action - stay in this case until the OpMode is stopped.
    // This method will still be called regardless of the state machine.
    //
    */
            default:
                //
                // The autonomous actions have been accomplished (i.e. the state has
                // transitioned into its final state.
                //
                break;
        }

        //
        // Send telemetry data to the driver station.
        //
        update_telemetry (); // Update common telemetry
        telemetry.addData ("18", "State: " + v_state);

    } // loop

    //--------------------------------------------------------------------------
    //
    // v_state
    //
    /**
     * This class member remembers which state is currently active.  When the
     * start method is called, the state will be initialized (0).  When the loop
     * starts, the state will change from initialize to state_1.  When state_1
     * actions are complete, the state will change to state_2.  This implements
     * a state machine for the loop method.
     */
    private int v_state = 0;

    public double cmtopulse (double distance_cm){
        double circumference = 10.16*Math.PI; // Pi*D
        double wheel_rotations = distance_cm/circumference*1; // wheel rotation = distance/circumference * gear ratio
        double pulses = wheel_rotations*1440; // Tetrix CPR - counts per revolution
        return pulses;
    }

    public double degreestopulse (int degrees){
        double wheel_base = 23.5*2; // spin turn
        double wheel_rotations = wheel_base*Math.PI/(10.16*Math.PI);

        double pulses = wheel_rotations*1440; // Tetrix CPR - counts per revolution

        return (pulses*degrees/360.0f);
    }



} // PushBotAuto


