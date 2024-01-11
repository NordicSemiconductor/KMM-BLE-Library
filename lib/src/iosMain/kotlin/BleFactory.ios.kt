/*
 * Copyright (c) 2023, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import advertisement.Advertiser
import advertisement.IOSServer
import advertisement.IOSServerWrapper
import client.Client
import client.IOSClient
import client.IOSClientWrapper
import scanner.Scanner
import server.NotificationsRecords
import server.Server

//TODO: check if something needs to be a singletone
actual object BleFactory {
    actual fun provideScanner(): Scanner {
        return Scanner(provideIOSClientWrapper())
    }

    actual fun provideAdvertiser(): Advertiser {
        return Advertiser(provideIOSServerWrapper())
    }

    actual fun provideClient(): Client {
        return Client(provideIOSClientWrapper())
    }

    actual fun provideServer(): Server {
        return Server(provideIOSServerWrapper())
    }

    private fun provideNotificationRecords(): NotificationsRecords {
        return NotificationsRecords()
    }

    private fun provideIOSServerWrapper(): IOSServerWrapper {
        return IOSServerWrapper(IOSServer(provideNotificationRecords()))
    }

    private fun provideIOSClientWrapper(): IOSClientWrapper {
        return IOSClientWrapper(IOSClient())
    }
}