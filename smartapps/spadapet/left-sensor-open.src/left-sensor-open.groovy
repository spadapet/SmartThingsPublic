definition(
    name: "Left Sensor Open",
    namespace: "spadapet",
    author: "Peter Spada",
    description: "Notifies you when you a sensor is open during a period of time.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/bon-voyage.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/bon-voyage%402x.png"
)

preferences
{
  section("Choose sensor")
  {
    input "contact", "capability.contactSensor"
  }

  section("Times")
  {
    input "startTime", "time", description: "Start time", required: true
    input "endTime", "time", description: "End time", required: true
  }

  section("Send text to")
  {
      input "phone1", "phone", title: "Phone number 1 (optional)", required: false
      input "phone2", "phone", title: "Phone number 2 (optional)", required: false
  }
}

def installed()
{
  log.trace "installed()"
  subscribe()
}

def updated()
{
  log.trace "updated()"
  unsubscribe()
  subscribe()
}

def subscribe()
{
  subscribe(contact, "contact.open", sensorOpen)
  runEvery1Hour(checkSensor)
  checkSensor()
}

def sensorOpen(evt)
{
  log.trace "doorOpen($evt.name: $evt.value)"
  checkSensor()
}

def checkSensor()
{
  def state = contact.currentState("contact")
  log.trace "checkSensor(), value=$state.value"

  if (state.value == "open" && timeOfDayIsBetween(startTime, endTime, new Date(), location.timeZone))
  {
    sendMessage()
  }
}

void sendMessage()
{
  def msg = "${contact.displayName} is open."
  log.info msg

  if (phone1)
  {
    sendSms phone1, msg
  }
  else
  {
    sendPush msg
  }
  
  if (phone2)
  {
    sendSms phone2, msg
  }
}
