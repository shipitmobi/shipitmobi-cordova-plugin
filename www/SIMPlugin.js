var SIMPlugin = {

	registerDevice: function(title, myteststring, buttonlable){
		cordova.exec(this.success, this.fail, 'SIMPlugin', 'registerDevice', [title, myteststring, buttonlable]);
	},

	/*
	 * Initialize device configuration and register for Push Notification.
	 * Function: initialize
	 * Platform: android
	 *
	 * Parameters:
	 * config	array		{
	 * 						 projectid:'XXXX' (Mandatory),
	 * 						 shipitAppID :'YYYY' (Mandatory),
	 * 						 buildType: "DEV/PROD" (Optional default PROD, useful for adding device as test device)
	 * 						}
	 * config	errorCB		callback function in case of error
	 * config	successCB	callback function in case of success
	*/
	initialize: function(config, errorCB, successCB) {
		if(errorCB == undefined)
			errorCB = this.fail;
		if(successCB == undefined)
			successCB = this.success;
		cordova.exec(successCB, errorCB, "SIMPlugin", "initialize", config ? [config] : []);
	},

	/*
	 * Enable/Disable Push notification for device.
	 * Function: notificationEnable
	 * Platform: android
	 *
	 * Parameters:
	 * config	array		{NotificationEnable:true/false}
	 * config	errorCB		callback function in case of error
	 * config	successCB	callback function in case of success
	*/
	notificationEnable: function(config, errorCB, successCB) {
		if(errorCB == undefined)
			errorCB = this.fail;
		if(successCB == undefined)
			successCB = this.success;
		cordova.exec(successCB, errorCB, "SIMPlugin", "pushEnable", config ? [config] : []);
	},

	findDataType: function(data)
	{
		console.log(data);
		if(typeof data == "string")
			return "String";
		if(data instanceof Date)
			return 'Date';
		if(typeof data == "number")
			return 'Integer';
		if(typeof data == "boolean")
			return 'Boolean';
		if(typeof data == Array)
			return 'List';
		return 0;
	},

	/*
	 * set a value of Tag for device.
	 * Function: getChannelID
	 *
	 * Parameters:
	 * config	errorCB		callback function in case of error
	 * config	successCB	callback function in case of success
	*/
	getChannelID: function(errorCB, successCB) {
		if(errorCB == undefined)
			errorCB = this.fail;
		if(successCB == undefined)
			successCB = this.success;
		cordova.exec(successCB, errorCB, "SIMPlugin", "getChannelID", []);
	},

	/*
	 * set a value of Tag for device.
	 * Function: getBundle
	 *
	 * Parameters:
	 * config	errorCB		callback function in case of error
	 * config	successCB	callback function in case of success
	*/
	getBundle: function(errorCB, successCB) {
		if(errorCB == undefined)
			errorCB = this.fail;
		if(successCB == undefined)
			successCB = this.success;
		cordova.exec(function(data){successCB(data?JSON.parse(data):data);},errorCB,"SIMPlugin","getBundle",[]);
	},

	/*
	 * set a value of Tag for device.
	 * Function: setTag
	 * Platform: android
	 *
	 * Parameters:
	 * config	array		{tagName:"MyTag",tagValue:'MyTagValue'}
	 * config	errorCB		callback function in case of error
	 * config	successCB	callback function in case of success
	*/
	setTag: function(config, errorCB, successCB) {
		if(errorCB == undefined)
			errorCB = this.fail;
		if(successCB == undefined)
			successCB = this.success;
		tags = config ? [config] : [];
		if((tags == undefined) || (tags.constructor !== Array) || !tags.length)
		{
			errorCB();
			return;
		}

		var type = 0;
		for(var i=0; i< tags.length; i++)
		{
			if(!(type = this.findDataType(tags[i]['tagValue'])))
			{
				console.log('Incorrect tag data ' + tags[i]);
				return;
			}
			tags[i]['tagType'] = type;
			if(type == 'Date')
				tags[i]['tagValue'] = tags[i]['tagValue'].getTime();
		}
		cordova.exec(successCB, errorCB, "SIMPlugin", "setTag", tags);
	},

	/*
	 * delete a tag from the device
	 * Function: deleteTag
	 * Platform: android
	 *
	 * Parameters:
	 * config	array		{tagName:"MyTag"}
	 * config	errorCB		callback function in case of error
	 * config	successCB	callback function in case of success
	*/
	deleteTag: function(config, errorCB, successCB) {
		if(errorCB == undefined)
			errorCB = this.fail;
		if(successCB == undefined)
			successCB = this.success;
		cordova.exec(errorCB, successCB, "SIMPlugin", "deleteTag", config ? [config] : []);
	},

	/*
	 * Default success callback
	 */
	success: function(){
		console.log("Success");
	},

	/*
	 * Default failure callback
	 */
	fail: function(error){
		console.log("Error", error);
	}

};

module.exports=SIMPlugin;
