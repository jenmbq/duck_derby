//
//  YESSGameContainerViewController.m
//  Quacky
//
//  Created by Song Zhang on 6/12/14.
//  Copyright (c) 2014 YESS. All rights reserved.
//

#import "YESSGameContainerViewController.h"

#define kGameURL @"http://jenmarks-wf.github.io/duck_derby/src/index.html"

@interface YESSGameContainerViewController () <UIWebViewDelegate>

@property (nonatomic, weak) IBOutlet UIActivityIndicatorView *gameIndicatorView;
@property (nonatomic, weak) IBOutlet UIWebView *gameWebView;

@end

@implementation YESSGameContainerViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // initialize the indicator view
    _gameIndicatorView.color = self.view.tintColor;
    
    // initialize the webview
    // have it just load the game's url for the time being...
    _gameWebView.delegate = self;
    NSURL *gameURL = [NSURL URLWithString:kGameURL];
    NSURLRequest *gameRequest = [NSURLRequest requestWithURL:gameURL];
    [_gameWebView loadRequest:gameRequest];
}

#pragma mark - UIWebViewDelegate Protocol Methods

- (void) webViewDidStartLoad:(UIWebView *)webView
{
    [_gameIndicatorView startAnimating];
}

- (void) webViewDidFinishLoad:(UIWebView *)webView
{
    [_gameIndicatorView stopAnimating];
}

- (void) webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
    NSLog(@"WebView failed with error...");
    [_gameIndicatorView stopAnimating];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
