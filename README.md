# Checkersum-Checker
A checksum checker made in Java using JavaFX, that supports a range of hashes.

**SUPPORTS**
* MD5
* SHA-1
* SHA-224
* SHA-256
* SHA-384
* SHA-512


<a class="github-button" href="https://github.com/TheRedSpy15/Checkersum-Checker/archive/master.zip" data-icon="octicon-cloud-download" data-size="large" aria-label="Download TheRedSpy15/Checkersum-Checker on GitHub">Download</a>

[![checksumcheckersnipe.jpg](https://s14.postimg.cc/k76ygo3o1/checksumcheckersnipe.jpg)](https://postimg.cc/image/97lr52d8t/)


## What's it used for?

You know when you try to download a file off the internet, or have a super important email and want to make sure nothing happened to it along the way to your router, or even tampered with while you were away? Well, checksums are basically a fingerprint for a file on your computer, and is extremely unique (not %100 however) to your specific file. Attempting to keep the same fingerprint for a file after it was modified, is EXTREMELY difficult.

SO... when you download launchcodes.exe from a website, look for a hash or something that says something like: MD5, SHA-1, or SHA-256. Those are hashes used to add a fingerprint to your file. Use those to compare against the copy locally on your computer, to make sure nothing bad happened to them.

## What's it NOT used for?

It will not protect you from viruses.

At-least, not when the original file was one. It will protect you from viruses/malware, when the virus/malware was added to the file in transit, or while you were away (assuming you know the orginal checksum associated with the file before hand).

### Java is required!
