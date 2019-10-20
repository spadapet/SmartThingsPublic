/**
 *  Arlo Motion Notification
 *
 *  Copyright 2017 Peter Spada
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Arlo Motion Notification",
    namespace: "spadapet",
    author: "Peter Spada",
    description: "Triggers audio notifications when motion is detected.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences
{
    section("Devices")
    {
        input "cameras", "capability.motionSensor", multiple: true
        input "doorbell", "capability.musicPlayer"
    }
}

def installed()
{
	initialize()
}

def updated()
{
	unsubscribe()
	initialize()
}

def initialize()
{
    log.debug "Initialize with settings: ${settings}"
    subscribe(cameras, "motion", motionStatusChanged)
}

def motionStatusChanged(evt)
{
    log.debug "${evt.device.displayName}: Motion ${evt.value}"
    if (evt.value == "active")
    {
        doorbell.playTrack(0)
    }
}
