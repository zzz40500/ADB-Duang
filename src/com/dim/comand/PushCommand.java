package com.dim.comand;

import com.dim.DeviceResult;

import java.io.File;
import java.util.List;


/**
 * Created by dim on 16/3/31.
 */
public class PushCommand
	extends Command {

	private final DeviceResult deviceResult;
	private final String localFilePath;
	private final String remoteFilePath;
	private final String remoteFileName;

	public PushCommand(DeviceResult deviceResult, String localFilePath, String remoteFilePath,
	                   String remoteFileName) {
		this.deviceResult = deviceResult;
		this.localFilePath = localFilePath;
		this.remoteFilePath = remoteFilePath.endsWith("/") ? remoteFilePath : remoteFilePath + "/";
		this.remoteFileName = remoteFileName;
	}

	@Override
	public boolean run() {
		ChmodCommand chmodCommand = new ChmodCommand(deviceResult, remoteFilePath);
		chmodCommand.run();
		try {
			deviceResult.device.pushFile(localFilePath, remoteFilePath + remoteFileName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		LsCommand lsCommand = new LsCommand(deviceResult, remoteFilePath, remoteFileName);
		lsCommand.run();
		List<String> result1 = lsCommand.getResult();
		if (result1.contains(new File(localFilePath).getName())) {
			return true;
		}

		return false;
	}
}
