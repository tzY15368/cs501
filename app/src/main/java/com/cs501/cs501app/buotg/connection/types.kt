package com.cs501.cs501app.buotg.connection

import com.cs501.cs501app.buotg.database.SyncData
import com.cs501.cs501app.buotg.database.entities.*
import java.util.*

open class StdResponse{
    lateinit var message: String
}

class LoginResponse: StdResponse(){
    lateinit var token: String
    lateinit var user: User
}

class EventResponse: StdResponse(){
    lateinit var Event: Event
}

class EventsResponse: StdResponse(){
    lateinit var Events: List<Event>
}

class SharedEventResponse: StdResponse(){
    lateinit var sharedEvent: SharedEvent
}

/**SEPs Shared Event Participance*/
class SEPsResponse: StdResponse(){
    lateinit var shared_event_participances: List<SharedEventParticipance>
}

class UserResponse: StdResponse(){
    lateinit var user: User
}

class GroupListResponse: StdResponse(){
    lateinit var groups: List<Group>
}

class GroupResponse: StdResponse(){
    lateinit var group: Group
}

class GroupMemberResponse: StdResponse(){
    lateinit var groupMembers: List<GroupMember>
}

class SharedEventListResponse: StdResponse(){
    lateinit var sharedEvents: List<SharedEvent>
}
/**GML Group Member List*/
class GMLResponse:StdResponse(){
    // TODO: backend returns a list of only the user's id, but the group_member class is made up of two ids
    lateinit var group_members: List<UUID>
}

class SignupResponse: StdResponse(){
    // echoes back the user so we can save it
    lateinit var user: User
}

class SyncResponse: StdResponse(){
    // diff
    lateinit var data: SyncData
    // ignore rm for now...
}

class InviteResponse: StdResponse(){
    lateinit var group_invites:List<GroupInvite>
}