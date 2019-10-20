/**
 *  Keen Vent Control
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
    name: "Keen Vent Control",
    namespace: "spadapet",
    author: "Peter Spada",
    description: "Opens Keen vents when the heat is off.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences
{
	section("Devices")
    {
		input "ecobee", "capability.thermostat"
        input "vents", "capability.switch", multiple: true
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
	subscribe(ecobee, "thermostatOperatingState", ecobeeStatusChanged)
    updateVents()
}

def ecobeeStatusChanged(evt)
{
    updateVents()
}

def updateVents()
{
    log.debug "Ecobee is ${ecobee.currentThermostatOperatingState}"
    
    if (ecobee.currentThermostatOperatingState == "heating")
    {
        log.debug "Closing vents"
        vents.off()
    }
    else
    {
        log.debug "Opening vents"
        vents.on();
        vents.setLevel(100)
    }
}
