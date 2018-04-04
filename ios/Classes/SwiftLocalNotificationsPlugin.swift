import Flutter
import UIKit
import UserNotifications

@available(iOS 10.0, *)
public class SwiftLocalNotificationsPlugin: NSObject, FlutterPlugin, UNUserNotificationCenterDelegate {
    static var CREATE_NOTIFICATION = "local_notifications_createNotification"
    static var REMOVE_NOTIFICATION = "local_notifications_removeNotification"
    static var CHANNEL_NAME = "plugins/local_notifications"
    static var CALLBACK_KEY = "callback_key"
    static var PAYLOAD_KEY = "payload_key"
    static var PRESENT_WHILE_APP_OPEN_KEY = "present_while_app_open_key"
    
    var channel = FlutterMethodChannel()
    
    init(channel: FlutterMethodChannel) {
        self.channel = channel
    }
    
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: CHANNEL_NAME, binaryMessenger: registrar.messenger())
        let instance = SwiftLocalNotificationsPlugin(channel: channel)
        registrar.addMethodCallDelegate(instance, channel: channel)
        let center = UNUserNotificationCenter.current()
        center.delegate = instance
        requestPermissionToShowNotifications()
    }
    
    static func requestPermissionToShowNotifications() {
        UNUserNotificationCenter.current().requestAuthorization(options:[.badge, .alert, .sound]) { (granted, error) in
            if granted {
                DispatchQueue.main.async(execute: {
                    UIApplication.shared.registerUserNotificationSettings(UIUserNotificationSettings(types: [UIUserNotificationType.alert, UIUserNotificationType.badge, UIUserNotificationType.sound] , categories: nil))
                })
            }
        }
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        let args : NSArray = call.arguments as! NSArray
        if (call.method == SwiftLocalNotificationsPlugin.CREATE_NOTIFICATION) {
            createNotification(args: args)
            result(nil)
        } else if (call.method == SwiftLocalNotificationsPlugin.REMOVE_NOTIFICATION) {
            let notifId : Int = args[0] as! Int
            removeNotification(id: notifId)
            result(nil)
        } else {
            customDebugPrint(text: "No method found with name '\(call.method)")
            result(FlutterMethodNotImplemented)
        }
    }
    struct LocalNotificationsError: Error {
        let message: String
        
        init(_ message: String) {
            self.message = message
        }
        
        public var localizedDescription: String {
            return message
        }
    }
    
    struct NotificationAction {
        var actionText = ""
        var payload = ""
        var callbackName = ""
        var launchesApp = true
    }
    
    struct Notification {
        var title = ""
        var content = ""
        var imageUrl = "" // TODO: currently not used
        var ticker = ""
        var importance = 0 // TODO: currently not used, does ios support this?
        var isOngoing = false // TODO: currently not used, does ios support this?
        var id = 1
        var presentWhileAppOpen = true
    }
    
    func createNotification(args: NSArray) {
        let title : String = args[0] as! String
        let contentStr : String = args[1] as! String
        let imageUrl : String = args[2] as! String
        let ticker : String = args[3] as! String
        let importance : Int = args[4] as! Int
        let isOngoing : Bool = args[5] as! Bool
        let id : Int = args[6] as! Int
        let presentWhileAppOpen = args[15] as! Bool
        let notification : Notification = Notification(title: title, content: contentStr, imageUrl: imageUrl, ticker: ticker, importance: importance, isOngoing: isOngoing, id: id, presentWhileAppOpen: presentWhileAppOpen)
        
        let callbackName : String = args[7] as! String
        let actionText : String = args[8] as! String
        let intentPayload : String = args[9] as! String
        let launchesApp : Bool = args[10] as! Bool
        let onNotificationClick : NotificationAction = NotificationAction(actionText: actionText, payload: intentPayload, callbackName: callbackName, launchesApp: launchesApp)
        
        let actionsCallbacks : NSArray = args[11] as! NSArray
        let actionsTexts : NSArray = args[12] as! NSArray
        let actionsIntentsPayload : NSArray = args[13] as! NSArray
        let actionsLaunchesApp : NSArray = args[14] as! NSArray
        var extraActions : Array<NotificationAction> = []
        for (index, _) in actionsCallbacks.enumerated() {
            let callback : String = actionsCallbacks[index] as! String
            let text : String = actionsTexts[index] as! String
            let payload : String = actionsIntentsPayload[index] as! String
            let launch : Bool = actionsLaunchesApp[index] as! Bool
            let newAction : NotificationAction = NotificationAction(actionText: text, payload: payload, callbackName: callback, launchesApp: launch)
            extraActions.append(newAction)
        }
        
        createIosNotification(notification: notification, onNotificationClick: onNotificationClick, extraActions: extraActions)
    }
    
    private func createIosNotification(notification: Notification, onNotificationClick: NotificationAction, extraActions: Array<NotificationAction>) {
        
        let center = UNUserNotificationCenter.current()
        let content = UNMutableNotificationContent()
        content.title = notification.title
        content.body = notification.content
        
        // TODO: support sound customization?
        content.sound = UNNotificationSound.default()
        
        // TODO: content.attachments for image?
        var actionIndex : Int = 0
        content.userInfo[String(actionIndex)] = [onNotificationClick.callbackName, onNotificationClick.payload]
        content.userInfo[SwiftLocalNotificationsPlugin.PRESENT_WHILE_APP_OPEN_KEY] = notification.presentWhileAppOpen
        actionIndex += 1
        
        // add extra actions
        var notifActions : Array<UNNotificationAction> = []
        for action : NotificationAction in extraActions {
            let options : UNNotificationActionOptions = action.launchesApp ? [.foreground] : []
            let newNotifAction : UNNotificationAction = UNNotificationAction(identifier: String(actionIndex), title: action.actionText, options: options)
            notifActions.append(newNotifAction)
            content.userInfo[String(actionIndex)] = [action.callbackName, action.payload]
            actionIndex += 1
        }
        
        // Define Category
        let categoryIdentifier = "localNotifications"
        let localNotificationsCategory = UNNotificationCategory(identifier: categoryIdentifier, actions: notifActions, intentIdentifiers: [], options: [])
        
        // Register Category
        center.setNotificationCategories([localNotificationsCategory])
        content.categoryIdentifier = categoryIdentifier
        
        let date = Date(timeIntervalSinceNow: 1)
        let triggerDate = Calendar.current.dateComponents([.year, .month, .day, .hour, .minute, .second], from: date)
        let trigger = UNCalendarNotificationTrigger(dateMatching: triggerDate, repeats: false)
        let request = UNNotificationRequest(identifier: String(notification.id), content: content, trigger: trigger)
        center.add(request)
    }
    
    func removeNotification(id: Int) {
        let center = UNUserNotificationCenter.current()
        center.removeDeliveredNotifications(withIdentifiers: [String(id)])
    }
    
    public func userNotificationCenter(_ center: UNUserNotificationCenter,
                                       willPresent: UNNotification,
                                       withCompletionHandler: @escaping (UNNotificationPresentationOptions)->()) {
        let showNotificationWhileAppOpen = willPresent.request.content.userInfo[SwiftLocalNotificationsPlugin.PRESENT_WHILE_APP_OPEN_KEY] as! Bool
        if (showNotificationWhileAppOpen) {
            withCompletionHandler([.alert, .sound, .badge])
        }
    }
    
    public func userNotificationCenter(_ center: UNUserNotificationCenter,
                                       didReceive response: UNNotificationResponse,
                                       withCompletionHandler: @escaping ()->()) {
        var id = response.actionIdentifier
        if (id == UNNotificationDefaultActionIdentifier) {
            id = String(0)
        }
        
        let value = response.notification.request.content.userInfo[id]
        if (value != nil) {
            let arrVal = value as! Array<String>
            let callback : String = arrVal[0]
            let payload : String = arrVal[1]
            if (callback != "") {
                customDebugPrint(text: "didReceive: calling \(callback)('\(payload)')")
                channel.invokeMethod(callback, arguments: payload)
            }
            else {
                customDebugPrint(text: "didReceive: callback name was null or empty")
            }
        }
        else {
            customDebugPrint(text: "didReceive: value was null")
        }
        
        withCompletionHandler()
    }
    
    func customDebugPrint(text : String) {
        #if DEBUG
            NSLog("LocalNotificationsPlugin: \(text)")
        #endif
    }
    
    
}
