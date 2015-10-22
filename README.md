


<!DOCTYPE html>
<html lang="en" class=" is-copy-enabled">
  <head prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# object: http://ogp.me/ns/object# article: http://ogp.me/ns/article# profile: http://ogp.me/ns/profile#">
    <meta charset='utf-8'>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Language" content="en">
    <meta name="viewport" content="width=1020">
    
    
    <title>check_as400/README.md at master · cjt74392/check_as400</title>
    <link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="GitHub">
    <link rel="fluid-icon" href="https://github.com/fluidicon.png" title="GitHub">
    <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-114.png">
    <link rel="apple-touch-icon" sizes="114x114" href="/apple-touch-icon-114.png">
    <link rel="apple-touch-icon" sizes="72x72" href="/apple-touch-icon-144.png">
    <link rel="apple-touch-icon" sizes="144x144" href="/apple-touch-icon-144.png">
    <meta property="fb:app_id" content="1401488693436528">

      <meta content="@github" name="twitter:site" /><meta content="summary" name="twitter:card" /><meta content="cjt74392/check_as400" name="twitter:title" /><meta content="check_as400 - Nagios plugin to monitor IBM System i (AS/400)" name="twitter:description" /><meta content="https://avatars3.githubusercontent.com/u/2269156?v=3&amp;s=400" name="twitter:image:src" />
      <meta content="GitHub" property="og:site_name" /><meta content="object" property="og:type" /><meta content="https://avatars3.githubusercontent.com/u/2269156?v=3&amp;s=400" property="og:image" /><meta content="cjt74392/check_as400" property="og:title" /><meta content="https://github.com/cjt74392/check_as400" property="og:url" /><meta content="check_as400 - Nagios plugin to monitor IBM System i (AS/400)" property="og:description" />
      <meta name="browser-stats-url" content="https://api.github.com/_private/browser/stats">
    <meta name="browser-errors-url" content="https://api.github.com/_private/browser/errors">
    <link rel="assets" href="https://assets-cdn.github.com/">
    <link rel="web-socket" href="wss://live.github.com/_sockets/MjI2OTE1Njo1ZGZlZDE1YWQ4YmUyYjdiNWE1ZTVlYzU4MDYxMDdhMTozZGE4ZmI3NGYyZjcxNzQwMTg2NTFlYzNjYWRjOWU5MmVhY2E1MmY2M2U4MTFmYmRmZDYzYzJkYWZiZGFjNTky--53878d9d912cee402d31ba2d5c2bd10285236a8f">
    <meta name="pjax-timeout" content="1000">
    <link rel="sudo-modal" href="/sessions/sudo_modal">

    <meta name="msapplication-TileImage" content="/windows-tile.png">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="selected-link" value="repo_source" data-pjax-transient>

    <meta name="google-site-verification" content="KT5gs8h0wvaagLKAVWq8bbeNwnZZK1r1XQysX3xurLU">
    <meta name="google-analytics" content="UA-3769691-2">

<meta content="collector.githubapp.com" name="octolytics-host" /><meta content="github" name="octolytics-app-id" /><meta content="6503201E:07C4:92A4F71:5628810A" name="octolytics-dimension-request_id" /><meta content="2269156" name="octolytics-actor-id" /><meta content="cjt74392" name="octolytics-actor-login" /><meta content="e45b6eb2e915ceec91b6784e3b1b8e0738bc6f67928701dad8dfa063c2194c5f" name="octolytics-actor-hash" />

<meta content="Rails, view, blob#show" data-pjax-transient="true" name="analytics-event" />


  <meta class="js-ga-set" name="dimension1" content="Logged In">
    <meta class="js-ga-set" name="dimension4" content="Current repo nav">




    <meta name="is-dotcom" content="true">
        <meta name="hostname" content="github.com">
    <meta name="user-login" content="cjt74392">

      <link rel="mask-icon" href="https://assets-cdn.github.com/pinned-octocat.svg" color="#4078c0">
      <link rel="icon" type="image/x-icon" href="https://assets-cdn.github.com/favicon.ico">

    <meta content="9338634f68d92468377a380ddbf3a33f0b4f2f2b" name="form-nonce" />

    <link crossorigin="anonymous" href="https://assets-cdn.github.com/assets/github-ce671588d7b6afbb8bc61feba5b37b4acc3341aec654ff7a2405b657922ff0c1.css" integrity="sha256-zmcViNe2r7uLxh/rpbN7SswzQa7GVP96JAW2V5Iv8ME=" media="all" rel="stylesheet" />
    <link crossorigin="anonymous" href="https://assets-cdn.github.com/assets/github2-8d66f9bfcef65682f8b799f2330467be836c483d58670c9388d40c9e4c0492a3.css" integrity="sha256-jWb5v872VoL4t5nyMwRnvoNsSD1YZwyTiNQMnkwEkqM=" media="all" rel="stylesheet" />
    
    
    


    <meta http-equiv="x-pjax-version" content="eed6b43c44e1649982da4bd50b919d68">

      
  <meta name="description" content="check_as400 - Nagios plugin to monitor IBM System i (AS/400)">
  <meta name="go-import" content="github.com/cjt74392/check_as400 git https://github.com/cjt74392/check_as400.git">

  <meta content="2269156" name="octolytics-dimension-user_id" /><meta content="cjt74392" name="octolytics-dimension-user_login" /><meta content="24081978" name="octolytics-dimension-repository_id" /><meta content="cjt74392/check_as400" name="octolytics-dimension-repository_nwo" /><meta content="true" name="octolytics-dimension-repository_public" /><meta content="false" name="octolytics-dimension-repository_is_fork" /><meta content="24081978" name="octolytics-dimension-repository_network_root_id" /><meta content="cjt74392/check_as400" name="octolytics-dimension-repository_network_root_nwo" />
  <link href="https://github.com/cjt74392/check_as400/commits/master.atom" rel="alternate" title="Recent Commits to check_as400:master" type="application/atom+xml">

  </head>


  <body class="logged_in   env-production linux vis-public page-blob">
    <a href="#start-of-content" tabindex="1" class="accessibility-aid js-skip-to-content">Skip to content</a>

    
    
    



      <div class="header header-logged-in true" role="banner">
  <div class="container clearfix">

    <a class="header-logo-invertocat" href="https://github.com/" data-hotkey="g d" aria-label="Homepage" data-ga-click="Header, go to dashboard, icon:logo">
  <span class="mega-octicon octicon-mark-github"></span>
</a>


      <div class="site-search repo-scope js-site-search" role="search">
          <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/cjt74392/check_as400/search" class="js-site-search-form" data-global-search-url="/search" data-repo-search-url="/cjt74392/check_as400/search" method="get"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
  <label class="js-chromeless-input-container form-control">
    <div class="scope-badge">This repository</div>
    <input type="text"
      class="js-site-search-focus js-site-search-field is-clearable chromeless-input"
      data-hotkey="s"
      name="q"
      placeholder="Search"
      aria-label="Search this repository"
      data-global-scope-placeholder="Search GitHub"
      data-repo-scope-placeholder="Search"
      tabindex="1"
      autocapitalize="off">
  </label>
</form>
      </div>

      <ul class="header-nav left" role="navigation">
        <li class="header-nav-item">
          <a href="/pulls" class="js-selected-navigation-item header-nav-link" data-ga-click="Header, click, Nav menu - item:pulls context:user" data-hotkey="g p" data-selected-links="/pulls /pulls/assigned /pulls/mentioned /pulls">
            Pull requests
</a>        </li>
        <li class="header-nav-item">
          <a href="/issues" class="js-selected-navigation-item header-nav-link" data-ga-click="Header, click, Nav menu - item:issues context:user" data-hotkey="g i" data-selected-links="/issues /issues/assigned /issues/mentioned /issues">
            Issues
</a>        </li>
          <li class="header-nav-item">
            <a class="header-nav-link" href="https://gist.github.com/" data-ga-click="Header, go to gist, text:gist">Gist</a>
          </li>
      </ul>

    
<ul class="header-nav user-nav right" id="user-links">
  <li class="header-nav-item">
      <span class="js-socket-channel js-updatable-content"
        data-channel="notification-changed:cjt74392"
        data-url="/notifications/header">
      <a href="/notifications" aria-label="You have unread notifications" class="header-nav-link notification-indicator tooltipped tooltipped-s" data-ga-click="Header, go to notifications, icon:unread" data-hotkey="g n">
          <span class="mail-status unread"></span>
          <span class="octicon octicon-bell"></span>
</a>  </span>

  </li>

  <li class="header-nav-item dropdown js-menu-container">
    <a class="header-nav-link tooltipped tooltipped-s js-menu-target" href="/new"
       aria-label="Create new…"
       data-ga-click="Header, create new, icon:add">
      <span class="octicon octicon-plus left"></span>
      <span class="dropdown-caret"></span>
    </a>

    <div class="dropdown-menu-content js-menu-content">
      <ul class="dropdown-menu dropdown-menu-sw">
        
<a class="dropdown-item" href="/new" data-ga-click="Header, create new repository">
  New repository
</a>


  <a class="dropdown-item" href="/organizations/new" data-ga-click="Header, create new organization">
    New organization
  </a>



  <div class="dropdown-divider"></div>
  <div class="dropdown-header">
    <span title="cjt74392/check_as400">This repository</span>
  </div>
    <a class="dropdown-item" href="/cjt74392/check_as400/issues/new" data-ga-click="Header, create new issue">
      New issue
    </a>
    <a class="dropdown-item" href="/cjt74392/check_as400/settings/collaboration" data-ga-click="Header, create new collaborator">
      New collaborator
    </a>

      </ul>
    </div>
  </li>

  <li class="header-nav-item dropdown js-menu-container">
    <a class="header-nav-link name tooltipped tooltipped-s js-menu-target" href="/cjt74392"
       aria-label="View profile and more"
       data-ga-click="Header, show menu, icon:avatar">
      <img alt="@cjt74392" class="avatar" height="20" src="https://avatars1.githubusercontent.com/u/2269156?v=3&amp;s=40" width="20" />
      <span class="dropdown-caret"></span>
    </a>

    <div class="dropdown-menu-content js-menu-content">
      <div class="dropdown-menu  dropdown-menu-sw">
        <div class=" dropdown-header header-nav-current-user css-truncate">
            Signed in as <strong class="css-truncate-target">cjt74392</strong>

        </div>


        <div class="dropdown-divider"></div>

          <a class="dropdown-item" href="/cjt74392" data-ga-click="Header, go to profile, text:your profile">
            Your profile
          </a>
        <a class="dropdown-item" href="/stars" data-ga-click="Header, go to starred repos, text:your stars">
          Your stars
        </a>
        <a class="dropdown-item" href="/explore" data-ga-click="Header, go to explore, text:explore">
          Explore
        </a>
          <a class="dropdown-item" href="/integrations" data-ga-click="Header, go to integrations, text:integrations">
            Integrations
          </a>
        <a class="dropdown-item" href="https://help.github.com" data-ga-click="Header, go to help, text:help">
          Help
        </a>

          <div class="dropdown-divider"></div>

          <a class="dropdown-item" href="/settings/profile" data-ga-click="Header, go to settings, icon:settings">
            Settings
          </a>

          <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/logout" class="logout-form" data-form-nonce="9338634f68d92468377a380ddbf3a33f0b4f2f2b" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="SUX+mEZP2lszi+e0Trx8GQPS/WCJi/FdLVhOO7Aok2cyzXVKa46aYrvW5t1AN7AESAPXR51/Aa/N9iKqzaipWw==" /></div>
            <button class="dropdown-item dropdown-signout" data-ga-click="Header, sign out, icon:logout">
              Sign out
            </button>
</form>
      </div>
    </div>
  </li>
</ul>


    
  </div>
</div>

      

      


    <div id="start-of-content" class="accessibility-aid"></div>

    <div id="js-flash-container">
</div>


    <div role="main" class="main-content">
        <div itemscope itemtype="http://schema.org/WebPage">
    <div class="pagehead repohead instapaper_ignore readability-menu">

      <div class="container">

        <div class="clearfix">
          

<ul class="pagehead-actions">

  <li>
      <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/notifications/subscribe" class="js-social-container" data-autosubmit="true" data-form-nonce="9338634f68d92468377a380ddbf3a33f0b4f2f2b" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="z/H1mFfhU4jUo/pQANQrAiDOhfEKz5Rbj7BsR/VwXQy84JasDstUb+d+s73BR6PswhPKnZHzYp5C30f0N50jmw==" /></div>    <input id="repository_id" name="repository_id" type="hidden" value="24081978" />

      <div class="select-menu js-menu-container js-select-menu">
        <a href="/cjt74392/check_as400/subscription"
          class="btn btn-sm btn-with-count select-menu-button js-menu-target" role="button" tabindex="0" aria-haspopup="true"
          data-ga-click="Repository, click Watch settings, action:blob#show">
          <span class="js-select-button">
            <span class="octicon octicon-eye"></span>
            Unwatch
          </span>
        </a>
        <a class="social-count js-social-count" href="/cjt74392/check_as400/watchers">
          1
        </a>

        <div class="select-menu-modal-holder">
          <div class="select-menu-modal subscription-menu-modal js-menu-content" aria-hidden="true">
            <div class="select-menu-header">
              <span class="select-menu-title">Notifications</span>
              <span class="octicon octicon-x js-menu-close" role="button" aria-label="Close"></span>
            </div>

            <div class="select-menu-list js-navigation-container" role="menu">

              <div class="select-menu-item js-navigation-item " role="menuitem" tabindex="0">
                <span class="select-menu-item-icon octicon octicon-check"></span>
                <div class="select-menu-item-text">
                  <input id="do_included" name="do" type="radio" value="included" />
                  <span class="select-menu-item-heading">Not watching</span>
                  <span class="description">Be notified when participating or @mentioned.</span>
                  <span class="js-select-button-text hidden-select-button-text">
                    <span class="octicon octicon-eye"></span>
                    Watch
                  </span>
                </div>
              </div>

              <div class="select-menu-item js-navigation-item selected" role="menuitem" tabindex="0">
                <span class="select-menu-item-icon octicon octicon octicon-check"></span>
                <div class="select-menu-item-text">
                  <input checked="checked" id="do_subscribed" name="do" type="radio" value="subscribed" />
                  <span class="select-menu-item-heading">Watching</span>
                  <span class="description">Be notified of all conversations.</span>
                  <span class="js-select-button-text hidden-select-button-text">
                    <span class="octicon octicon-eye"></span>
                    Unwatch
                  </span>
                </div>
              </div>

              <div class="select-menu-item js-navigation-item " role="menuitem" tabindex="0">
                <span class="select-menu-item-icon octicon octicon-check"></span>
                <div class="select-menu-item-text">
                  <input id="do_ignore" name="do" type="radio" value="ignore" />
                  <span class="select-menu-item-heading">Ignoring</span>
                  <span class="description">Never be notified.</span>
                  <span class="js-select-button-text hidden-select-button-text">
                    <span class="octicon octicon-mute"></span>
                    Stop ignoring
                  </span>
                </div>
              </div>

            </div>

          </div>
        </div>
      </div>
</form>
  </li>

  <li>
    
  <div class="js-toggler-container js-social-container starring-container on">

    <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/cjt74392/check_as400/unstar" class="js-toggler-form starred js-unstar-button" data-form-nonce="9338634f68d92468377a380ddbf3a33f0b4f2f2b" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="XsJldoZTY6HkHLwnKrzZSkQMgWQRzpR1E3m5IN9zpzZpF0VsTSkgG8gjtZ27e5ue5WUUleo5kHv+eF2LQUoh6g==" /></div>
      <button
        class="btn btn-sm btn-with-count js-toggler-target"
        aria-label="Unstar this repository" title="Unstar cjt74392/check_as400"
        data-ga-click="Repository, click unstar button, action:blob#show; text:Unstar">
        <span class="octicon octicon-star"></span>
        Unstar
      </button>
        <a class="social-count js-social-count" href="/cjt74392/check_as400/stargazers">
          3
        </a>
</form>
    <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/cjt74392/check_as400/star" class="js-toggler-form unstarred js-star-button" data-form-nonce="9338634f68d92468377a380ddbf3a33f0b4f2f2b" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="oaVfWtaVr7RI7EAkoyXVWFVCMksbRSFBUt4NsL7DtaLPntuA0nYhA0OdAY737BTHNTfpMIJ30QR0Pp2b9Pj0AQ==" /></div>
      <button
        class="btn btn-sm btn-with-count js-toggler-target"
        aria-label="Star this repository" title="Star cjt74392/check_as400"
        data-ga-click="Repository, click star button, action:blob#show; text:Star">
        <span class="octicon octicon-star"></span>
        Star
      </button>
        <a class="social-count js-social-count" href="/cjt74392/check_as400/stargazers">
          3
        </a>
</form>  </div>

  </li>

  <li>
          <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/cjt74392/check_as400/fork" class="btn-with-count" data-form-nonce="9338634f68d92468377a380ddbf3a33f0b4f2f2b" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="cJ0eGsubxk7/DDycaPBvv6sS8HbWZFsRdykokLNkjRyf1nVqTFg/JJCdaTSTO8Ada7ta8mpHb7CHQCzE/gHKng==" /></div>
            <button
                type="submit"
                class="btn btn-sm btn-with-count"
                data-ga-click="Repository, show fork modal, action:blob#show; text:Fork"
                title="Fork your own copy of cjt74392/check_as400 to your account"
                aria-label="Fork your own copy of cjt74392/check_as400 to your account">
              <span class="octicon octicon-repo-forked"></span>
              Fork
            </button>
</form>
    <a href="/cjt74392/check_as400/network" class="social-count">
      1
    </a>
  </li>
</ul>

          <h1 itemscope itemtype="http://data-vocabulary.org/Breadcrumb" class="entry-title public ">
  <span class="mega-octicon octicon-repo"></span>
  <span class="author"><a href="/cjt74392" class="url fn" itemprop="url" rel="author"><span itemprop="title">cjt74392</span></a></span><!--
--><span class="path-divider">/</span><!--
--><strong><a href="/cjt74392/check_as400" data-pjax="#js-repo-pjax-container">check_as400</a></strong>

  <span class="page-context-loader">
    <img alt="" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
  </span>

</h1>

        </div>
      </div>
    </div>

    <div class="container">
      <div class="repository-with-sidebar repo-container new-discussion-timeline ">
        <div class="repository-sidebar clearfix">
          
<nav class="sunken-menu repo-nav js-repo-nav js-sidenav-container-pjax js-octicon-loaders"
     role="navigation"
     data-pjax="#js-repo-pjax-container"
     data-issue-count-url="/cjt74392/check_as400/issues/counts">
  <ul class="sunken-menu-group">
    <li class="tooltipped tooltipped-w" aria-label="Code">
      <a href="/cjt74392/check_as400" aria-label="Code" aria-selected="true" class="js-selected-navigation-item selected sunken-menu-item" data-hotkey="g c" data-selected-links="repo_source repo_downloads repo_commits repo_releases repo_tags repo_branches /cjt74392/check_as400">
        <span class="octicon octicon-code"></span> <span class="full-word">Code</span>
        <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>    </li>

      <li class="tooltipped tooltipped-w" aria-label="Issues">
        <a href="/cjt74392/check_as400/issues" aria-label="Issues" class="js-selected-navigation-item sunken-menu-item" data-hotkey="g i" data-selected-links="repo_issues repo_labels repo_milestones /cjt74392/check_as400/issues">
          <span class="octicon octicon-issue-opened"></span> <span class="full-word">Issues</span>
          <span class="js-issue-replace-counter"></span>
          <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>      </li>

    <li class="tooltipped tooltipped-w" aria-label="Pull requests">
      <a href="/cjt74392/check_as400/pulls" aria-label="Pull requests" class="js-selected-navigation-item sunken-menu-item" data-hotkey="g p" data-selected-links="repo_pulls /cjt74392/check_as400/pulls">
          <span class="octicon octicon-git-pull-request"></span> <span class="full-word">Pull requests</span>
          <span class="js-pull-replace-counter"></span>
          <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>    </li>

      <li class="tooltipped tooltipped-w" aria-label="Wiki">
        <a href="/cjt74392/check_as400/wiki" aria-label="Wiki" class="js-selected-navigation-item sunken-menu-item" data-hotkey="g w" data-selected-links="repo_wiki /cjt74392/check_as400/wiki">
          <span class="octicon octicon-book"></span> <span class="full-word">Wiki</span>
          <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>      </li>
  </ul>
  <div class="sunken-menu-separator"></div>
  <ul class="sunken-menu-group">

    <li class="tooltipped tooltipped-w" aria-label="Pulse">
      <a href="/cjt74392/check_as400/pulse" aria-label="Pulse" class="js-selected-navigation-item sunken-menu-item" data-selected-links="pulse /cjt74392/check_as400/pulse">
        <span class="octicon octicon-pulse"></span> <span class="full-word">Pulse</span>
        <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>    </li>

    <li class="tooltipped tooltipped-w" aria-label="Graphs">
      <a href="/cjt74392/check_as400/graphs" aria-label="Graphs" class="js-selected-navigation-item sunken-menu-item" data-selected-links="repo_graphs repo_contributors /cjt74392/check_as400/graphs">
        <span class="octicon octicon-graph"></span> <span class="full-word">Graphs</span>
        <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>    </li>
  </ul>


    <div class="sunken-menu-separator"></div>
    <ul class="sunken-menu-group">
      <li class="tooltipped tooltipped-w" aria-label="Settings">
        <a href="/cjt74392/check_as400/settings" aria-label="Settings" class="js-selected-navigation-item sunken-menu-item" data-selected-links="repo_settings repo_branch_settings hooks /cjt74392/check_as400/settings">
          <span class="octicon octicon-gear"></span> <span class="full-word">Settings</span>
          <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>      </li>
    </ul>
</nav>

            <div class="only-with-full-nav">
                
<div class="js-clone-url clone-url open"
  data-protocol-type="http">
  <h3 class="text-small text-muted"><span class="text-emphasized">HTTPS</span> clone URL</h3>
  <div class="input-group js-zeroclipboard-container">
    <input type="text" class="input-mini text-small text-muted input-monospace js-url-field js-zeroclipboard-target"
           value="https://github.com/cjt74392/check_as400.git" readonly="readonly" aria-label="HTTPS clone URL">
    <span class="input-group-button">
      <button aria-label="Copy to clipboard" class="js-zeroclipboard btn btn-sm zeroclipboard-button tooltipped tooltipped-s" data-copied-hint="Copied!" type="button"><span class="octicon octicon-clippy"></span></button>
    </span>
  </div>
</div>

  
<div class="js-clone-url clone-url "
  data-protocol-type="ssh">
  <h3 class="text-small text-muted"><span class="text-emphasized">SSH</span> clone URL</h3>
  <div class="input-group js-zeroclipboard-container">
    <input type="text" class="input-mini text-small text-muted input-monospace js-url-field js-zeroclipboard-target"
           value="git@github.com:cjt74392/check_as400.git" readonly="readonly" aria-label="SSH clone URL">
    <span class="input-group-button">
      <button aria-label="Copy to clipboard" class="js-zeroclipboard btn btn-sm zeroclipboard-button tooltipped tooltipped-s" data-copied-hint="Copied!" type="button"><span class="octicon octicon-clippy"></span></button>
    </span>
  </div>
</div>

  
<div class="js-clone-url clone-url "
  data-protocol-type="subversion">
  <h3 class="text-small text-muted"><span class="text-emphasized">Subversion</span> checkout URL</h3>
  <div class="input-group js-zeroclipboard-container">
    <input type="text" class="input-mini text-small text-muted input-monospace js-url-field js-zeroclipboard-target"
           value="https://github.com/cjt74392/check_as400" readonly="readonly" aria-label="Subversion checkout URL">
    <span class="input-group-button">
      <button aria-label="Copy to clipboard" class="js-zeroclipboard btn btn-sm zeroclipboard-button tooltipped tooltipped-s" data-copied-hint="Copied!" type="button"><span class="octicon octicon-clippy"></span></button>
    </span>
  </div>
</div>



<div class="clone-options text-small text-muted">You can clone with
  <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/users/set_protocol?protocol_selector=http&amp;protocol_type=push" class="inline-form js-clone-selector-form is-enabled" data-form-nonce="9338634f68d92468377a380ddbf3a33f0b4f2f2b" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="QSbq6maEjAAWpOHAR8LnVigHViq4UB3UPXv3gh3nIgKr0EX5POct89cq4Xyf8yMYa0VAXygW6odiCryF8rJDQg==" /></div><button class="btn-link js-clone-selector" data-protocol="http" type="submit">HTTPS</button></form>, <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/users/set_protocol?protocol_selector=ssh&amp;protocol_type=push" class="inline-form js-clone-selector-form is-enabled" data-form-nonce="9338634f68d92468377a380ddbf3a33f0b4f2f2b" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="N1j9Y3zwEzEqbO6gL1NV8nfqBopgico5VB7cxiXkBMOKRlQlppIrMaoZmZjbV6qN5iVl05/eq/mMlz7FArNZyQ==" /></div><button class="btn-link js-clone-selector" data-protocol="ssh" type="submit">SSH</button></form>, or <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/users/set_protocol?protocol_selector=subversion&amp;protocol_type=push" class="inline-form js-clone-selector-form is-enabled" data-form-nonce="9338634f68d92468377a380ddbf3a33f0b4f2f2b" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="8ulV2akHZGEi1oJusXf0pxOKdG4E+XKty8anWnDrh32VEUXB58hE6NgSNHCz31Fyg48XT3NPCB8kGsg7H34/RA==" /></div><button class="btn-link js-clone-selector" data-protocol="subversion" type="submit">Subversion</button></form>.
  <a href="https://help.github.com/articles/which-remote-url-should-i-use" class="help tooltipped tooltipped-n" aria-label="Get help on which URL is right for you.">
    <span class="octicon octicon-question"></span>
  </a>
</div>

              <a href="/cjt74392/check_as400/archive/master.zip"
                 class="btn btn-sm sidebar-button"
                 aria-label="Download the contents of cjt74392/check_as400 as a zip file"
                 title="Download the contents of cjt74392/check_as400 as a zip file"
                 rel="nofollow">
                <span class="octicon octicon-cloud-download"></span>
                Download ZIP
              </a>
            </div>
        </div>
        <div id="js-repo-pjax-container" class="repository-content context-loader-container" data-pjax-container>

          

<a href="/cjt74392/check_as400/blob/af60b5abe5f1295cf7cab3d3222589d766a5743d/README.md" class="hidden js-permalink-shortcut" data-hotkey="y">Permalink</a>

<!-- blob contrib key: blob_contributors:v21:80dfd11b4e1675a5d5c177b195d442c0 -->

  <div class="file-navigation js-zeroclipboard-container">
    
<div class="select-menu js-menu-container js-select-menu left">
  <button class="btn btn-sm select-menu-button js-menu-target css-truncate" data-hotkey="w"
    title="master"
    type="button" aria-label="Switch branches or tags" tabindex="0" aria-haspopup="true">
    <i>Branch:</i>
    <span class="js-select-button css-truncate-target">master</span>
  </button>

  <div class="select-menu-modal-holder js-menu-content js-navigation-container" data-pjax aria-hidden="true">

    <div class="select-menu-modal">
      <div class="select-menu-header">
        <span class="select-menu-title">Switch branches/tags</span>
        <span class="octicon octicon-x js-menu-close" role="button" aria-label="Close"></span>
      </div>

      <div class="select-menu-filters">
        <div class="select-menu-text-filter">
          <input type="text" aria-label="Find or create a branch…" id="context-commitish-filter-field" class="js-filterable-field js-navigation-enable" placeholder="Find or create a branch…">
        </div>
        <div class="select-menu-tabs">
          <ul>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="branches" data-filter-placeholder="Find or create a branch…" class="js-select-menu-tab" role="tab">Branches</a>
            </li>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="tags" data-filter-placeholder="Find a tag…" class="js-select-menu-tab" role="tab">Tags</a>
            </li>
          </ul>
        </div>
      </div>

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="branches" role="menu">

        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


            <a class="select-menu-item js-navigation-item js-navigation-open selected"
               href="/cjt74392/check_as400/blob/master/README.md"
               data-name="master"
               data-skip-pjax="true"
               rel="nofollow">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <span class="select-menu-item-text css-truncate-target" title="master">
                master
              </span>
            </a>
        </div>

          <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/cjt74392/check_as400/branches" class="js-create-branch select-menu-item select-menu-new-item-form js-navigation-item js-new-item-form" data-form-nonce="9338634f68d92468377a380ddbf3a33f0b4f2f2b" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="z6q8sEnW921q/8TlkItY6ffaamaOHdod33s/OUEiWlE3oP7YeA7JpLqHLZZEs2q9kiNsdWL7b4j6L3yWPoTWaQ==" /></div>
            <span class="octicon octicon-git-branch select-menu-item-icon"></span>
            <div class="select-menu-item-text">
              <span class="select-menu-item-heading">Create branch: <span class="js-new-item-name"></span></span>
              <span class="description">from ‘master’</span>
            </div>
            <input type="hidden" name="name" id="name" class="js-new-item-value">
            <input type="hidden" name="branch" id="branch" value="master">
            <input type="hidden" name="path" id="path" value="README.md">
</form>
      </div>

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="tags">
        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


        </div>

        <div class="select-menu-no-results">Nothing to show</div>
      </div>

    </div>
  </div>
</div>

    <div class="btn-group right">
      <a href="/cjt74392/check_as400/find/master"
            class="js-show-file-finder btn btn-sm empty-icon tooltipped tooltipped-nw"
            data-pjax
            data-hotkey="t"
            aria-label="Quickly jump between files">
        <span class="octicon octicon-list-unordered"></span>
      </a>
      <button aria-label="Copy file path to clipboard" class="js-zeroclipboard btn btn-sm zeroclipboard-button tooltipped tooltipped-s" data-copied-hint="Copied!" type="button"><span class="octicon octicon-clippy"></span></button>
    </div>

    <div class="breadcrumb js-zeroclipboard-target">
      <span class="repo-root js-repo-root"><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/cjt74392/check_as400" class="" data-branch="master" data-pjax="true" itemscope="url"><span itemprop="title">check_as400</span></a></span></span><span class="separator">/</span><strong class="final-path">README.md</strong>
    </div>
  </div>

<include-fragment class="commit-tease" src="/cjt74392/check_as400/contributors/master/README.md">
  <div>
    Fetching contributors&hellip;
  </div>

  <div class="commit-tease-contributors">
    <img alt="" class="loader-loading left" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32-EAF2F5.gif" width="16" />
    <span class="loader-error">Cannot retrieve contributors at this time</span>
  </div>
</include-fragment>
<div class="file">
  <div class="file-header">
  <div class="file-actions">

    <div class="btn-group">
      <a href="/cjt74392/check_as400/raw/master/README.md" class="btn btn-sm " id="raw-url">Raw</a>
        <a href="/cjt74392/check_as400/blame/master/README.md" class="btn btn-sm js-update-url-with-hash">Blame</a>
      <a href="/cjt74392/check_as400/commits/master/README.md" class="btn btn-sm " rel="nofollow">History</a>
    </div>


        <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/cjt74392/check_as400/edit/master/README.md" class="inline-form js-update-url-with-hash" data-form-nonce="9338634f68d92468377a380ddbf3a33f0b4f2f2b" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="+gE1qGUsdst0I6YOV5dWnTNsPqGhMVSHRDUyyE9iUHv8RBXgJFCy89w4Y7gQ4088nvZm7xpIiPxF5kbY3C1T2g==" /></div>
          <button class="octicon-btn tooltipped tooltipped-nw" type="submit"
            aria-label="Edit this file" data-hotkey="e" data-disable-with>
            <span class="octicon octicon-pencil"></span>
          </button>
</form>        <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/cjt74392/check_as400/delete/master/README.md" class="inline-form" data-form-nonce="9338634f68d92468377a380ddbf3a33f0b4f2f2b" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="wHjJ8ST6vlXp54rWG5jhDVQkOodUaW7ffmmZYOiren6/EINiR0zZbeQj5qWK3sXJ6/51/h85PkP57S+V3+kh0w==" /></div>
          <button class="octicon-btn octicon-btn-danger tooltipped tooltipped-nw" type="submit"
            aria-label="Delete this file" data-disable-with>
            <span class="octicon octicon-trashcan"></span>
          </button>
</form>  </div>

  <div class="file-info">
      123 lines (104 sloc)
      <span class="file-info-divider"></span>
    6.55 KB
  </div>
</div>

  
  <div id="readme" class="blob instapaper_body">
    <article class="markdown-body entry-content" itemprop="mainContentOfPage"><h1><a id="user-content-check_as400" class="anchor" href="#check_as400" aria-hidden="true"><span class="octicon octicon-link"></span></a>check_as400</h1>

<p>Nagios plugin to monitor IBM System i (AS/400)</p>

<pre><code>  AJ                = Number of active jobs in system.
  CJ &lt;job&gt; [-w -c]  = Check to see if job &lt;job&gt; is in the system.[Number of ACTIVE &lt;job&gt;]
  CJS &lt;sbs&gt; &lt;job&gt; [status &lt;STATUS&gt;] [noperm]
                    = Check to see if job is existing in Subsystem and has this status.
                      Job checking can be controlled by :
                      status &lt;status&gt;       = any other status goes to critical
                      noperm                = don't go to critical if job is not in the system
                      NOTE: if JobStatus is set, it has highest Priority
  JOBS              = Number of jobs in system.
  JOBQ &lt;lib/jobq&gt;   = Number of jobs in JOBQ.
  CPU               = CPU load.
  CPUC &lt;cpuBase&gt;    = CPU load, Consider Current processing capacity. (CPU used * VP nums / cpuBase).
                      NOTE: Specify &lt;cpuBase&gt;, EX: You want use 3 CPU only, but VP use more than 3.
  CPUT &lt;job&gt;        = Top CPU used job. The total processing unit time used by the job
                      Specify job name, ex: *ALL or QZ* or QZDASOINIT
  US                = Percent free storage
  ASP &lt;aspNum&gt;      = Check ASP &lt;aspNum&gt; used
  DISK              = Check DISK Status.
  DB                = DB utilization. (Not available after V6R1)
  DBFault           = Pool DB/Non-DB Fault
  LOGIN             = Check if login completes.
  MSG &lt;user&gt;        = Check for any unanswered messages on msg queue &lt;user&gt;
                      Any unanswered messages causes warning status.
  OUTQ &lt;queue&gt;      = Check outq files, writer and status. No writer, or
                      status of 'HLD' causes warning status. This default
                      behavior can be modified with the following options:
                         nw    = Don't go critical when no writer
                         ns    = Don't warn if status is 'HLD'
                         nf    = Ignore number of files in queue
                      NOTE: threshold values are used on number of files
  SBS &lt;subsystem&gt;   = Check if the subsystem &lt;subsystem&gt; is running.
                      NOTE: specify &lt;subsystem&gt; as library/subsystem
  PRB               = Check if the problem was identified.
  FDN               = Number of file members; specify library/filename
  ---------- VISION MIMIX ----------
  MIMIX &lt;DG name&gt;   = Check MIMIX Data Group Unprocessed Entry Count, Transfer definition, RJ link state.
  ---------- Rocket iCluster ----------
  ICNODE            = Check for any Inactive or Failed Node status.
  ICGROUP           = Check for any Inactive or Indoubt Group status.
  ICSWTCHRDY &lt;grp&gt;  = Check for multiple conditions for switch readiness.
</code></pre>

<h1></h1>

<h1><a id="user-content-install-note-" class="anchor" href="#install-note-" aria-hidden="true"><span class="octicon octicon-link"></span></a>Install Note </h1>

<ul>
<li>Modify your $NAGIOS_PATH/.as400 with the correct user and password.(User name &lt;= 9 characters)</li>
<li>Modify chech_as400 with the correct java path. </li>
<li>Set NAGIOS user profile Display sign-on information *NO </li>
<li>CHGUSRPRF USRPRF(NAGIOS) DSPSGNINF(*NO) 
Initial program to call  . . . .   *NONE
Initial menu . . . . . . . . . .   MAIN</li>
<li>For languages other than English , change system library list to ENGLISH.</li>
<li>Nagios user may need to use IBM default sign-on display file
Or change nagiso user to other subsystem description to use system default display file(QSYS/QDSIGNON)</li>
<li>Avoid the massive entries log
CHGJOBD JOBD(NAGIOSJOBD) LOG(4 00 *MSG)
*Use SSL connection
Must add CE to JAVA, EX:
keytool -import -trustcacerts -keystore /usr/lib/jvm/java-1.7.0-openjdk-1.7.0.85.x86_64/jre/lib/security/cacerts -storepass changeit -noprompt -alias xxxx_ce -file /xx/xxx/XXXX.cer</li>
</ul>

<h2><a id="user-content-preinstall-notes" class="anchor" href="#preinstall-notes" aria-hidden="true"><span class="octicon octicon-link"></span></a>Preinstall notes</h2>

<ul>
<li>Security Note: Realize that this plugin communicates to the AS400 via telnet, which is easy to sniff and capture user names and passwords.  Use a generic user with restrictive rights for the plugin. The user needs access to wrksyssts, wrkoutq, wrkactjob, dspjob, dspsbsd and dspmsg.</li>
</ul>

<h2><a id="user-content-quick-and-easy" class="anchor" href="#quick-and-easy" aria-hidden="true"><span class="octicon octicon-link"></span></a>Quick And Easy</h2>

<p>1) For languages other than English you will need to recompile the plugin before continuing.  Refer to steps 1 and 2 of the Manual installation section below.</p>

<ul>
<li>Type
./install</li>
</ul>

<p>2) Modify your $NAGIOS_PATH/libexec/.as400 with the correct user and password
3) Add the contents of the checkcommands.example file into your $NAGIOS/etc/checkcommands.cfg
4) Add services to your nagios configuration.  There are examples in the services.example file.
5) Add dependencies to your nagios configuration.  There are examples in the dependency.example file.
6) DONE!</p>

<h2><a id="user-content-manual-installation" class="anchor" href="#manual-installation" aria-hidden="true"><span class="octicon octicon-link"></span></a>Manual Installation</h2>

<p>1) For languages other than English ,  change system library list to ENGLISH
     &lt;1&gt;.Login AS/400 Nagios check ID.
     &lt;2&gt;.CHGSYSLIBL LIB(QSYS2984) OPTION(*ADD)
     &lt;3&gt;Logout</p>

<pre><code>&lt;Option&gt; If you still want to use other languages, you will need to create and recompile  the language before continuing.
    &lt;1&gt;.rm check_as400_lang.java
    &lt;2&gt;.ln -s langs/check_as400_lang_(your language).java .
    &lt;3&gt;.javac check_as400_lang.java
</code></pre>

<p>2) Modify chech_as400 with the correct java path.</p>

<p>3) Compile the plugin:
         javac check_as400.java</p>

<p>4) Copy all .class files to nagios/libexec
         cp *.class /usr/local/nagios/libexec</p>

<p>5) EDIT and Copy the check_as400 script to nagios/libexec
         cp check_as400 /usr/local/nagios/libexec
         vi /usr/local/nagios/libexec/check_as400</p>

<ul>
<li>Make sure you modify the JAVA_START path in the check_as400 script.</li>
</ul>

<p>6) Create a file in /usr/local/nagios/libexec called .as400, with contents matching that of he file example.as400.  This file contains the user name and password used to login to the AS/400.</p>

<p>7) Change the user and group of these files to your nagios user:
        cd /usr/local/nagios/libexec
        chown nagios:nagios *.class check_as400 .as400
        chmod 700 .as400</p>

<ul>
<li>Security Note! Set the permissions of the /usr/local/nagios/libexec/.as400 file.  This way only the nagios user can read the contents of this file.</li>
</ul>

<p>8) Add Nagios configuration</p>

<ul>
<li>Add the contents of checkcommands.example to your Nagios checkcommands.cfg</li>
<li>Add services to your nagios configuration.  There are examples in the services.example file.</li>
<li>Add dependencies to your nagios configuration.  There are examples in the dependency.example file.</li>
</ul>

<p>9) Set AS/400 NAGIOS user profile Display sign-on information <em>NO
CHGUSRPRF USRPRF(NAGIOS) DSPSGNINF(</em>NO)</p>

<p>Enjoy!</p>
</article>
  </div>

</div>

<a href="#jump-to-line" rel="facebox[.linejump]" data-hotkey="l" style="display:none">Jump to Line</a>
<div id="jump-to-line" style="display:none">
  <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="" class="js-jump-to-line-form" method="get"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
    <input class="linejump-input js-jump-to-line-field" type="text" placeholder="Jump to line&hellip;" aria-label="Jump to line" autofocus>
    <button type="submit" class="btn">Go</button>
</form></div>

        </div>
      </div>
      <div class="modal-backdrop"></div>
    </div>
  </div>


    </div>

      <div class="container">
  <div class="site-footer" role="contentinfo">
    <ul class="site-footer-links right">
        <li><a href="https://status.github.com/" data-ga-click="Footer, go to status, text:status">Status</a></li>
      <li><a href="https://developer.github.com" data-ga-click="Footer, go to api, text:api">API</a></li>
      <li><a href="https://training.github.com" data-ga-click="Footer, go to training, text:training">Training</a></li>
      <li><a href="https://shop.github.com" data-ga-click="Footer, go to shop, text:shop">Shop</a></li>
        <li><a href="https://github.com/blog" data-ga-click="Footer, go to blog, text:blog">Blog</a></li>
        <li><a href="https://github.com/about" data-ga-click="Footer, go to about, text:about">About</a></li>
        <li><a href="https://github.com/pricing" data-ga-click="Footer, go to pricing, text:pricing">Pricing</a></li>

    </ul>

    <a href="https://github.com" aria-label="Homepage">
      <span class="mega-octicon octicon-mark-github" title="GitHub"></span>
</a>
    <ul class="site-footer-links">
      <li>&copy; 2015 <span title="0.08818s from github-fe136-cp1-prd.iad.github.net">GitHub</span>, Inc.</li>
        <li><a href="https://github.com/site/terms" data-ga-click="Footer, go to terms, text:terms">Terms</a></li>
        <li><a href="https://github.com/site/privacy" data-ga-click="Footer, go to privacy, text:privacy">Privacy</a></li>
        <li><a href="https://github.com/security" data-ga-click="Footer, go to security, text:security">Security</a></li>
        <li><a href="https://github.com/contact" data-ga-click="Footer, go to contact, text:contact">Contact</a></li>
        <li><a href="https://help.github.com" data-ga-click="Footer, go to help, text:help">Help</a></li>
    </ul>
  </div>
</div>



    
    
    

    <div id="ajax-error-message" class="flash flash-error">
      <span class="octicon octicon-alert"></span>
      <button type="button" class="flash-close js-flash-close js-ajax-error-dismiss" aria-label="Dismiss error">
        <span class="octicon octicon-x"></span>
      </button>
      Something went wrong with that request. Please try again.
    </div>


      <script crossorigin="anonymous" integrity="sha256-Fhzsf0y5oYf2bC7Lj1nJCY4q1kRYr5F+xy+dYda4CJs=" src="https://assets-cdn.github.com/assets/frameworks-161cec7f4cb9a187f66c2ecb8f59c9098e2ad64458af917ec72f9d61d6b8089b.js"></script>
      <script async="async" crossorigin="anonymous" integrity="sha256-HAMfxaEMns0lj627XVmHi8PMISeiJ6TQGbI+f6piK1g=" src="https://assets-cdn.github.com/assets/github-1c031fc5a10c9ecd258fadbb5d59878bc3cc2127a227a4d019b23e7faa622b58.js"></script>
      
      
    <div class="js-stale-session-flash stale-session-flash flash flash-warn flash-banner hidden">
      <span class="octicon octicon-alert"></span>
      <span class="signed-in-tab-flash">You signed in with another tab or window. <a href="">Reload</a> to refresh your session.</span>
      <span class="signed-out-tab-flash">You signed out in another tab or window. <a href="">Reload</a> to refresh your session.</span>
    </div>
  </body>
</html>

