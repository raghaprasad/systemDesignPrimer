1. I can view video content on demand 
2. I can search for video titles from a catalog 
--
3. Rate video 
4. Closed captions
5. X-ray integration etc, etc 

----- 

## Data Model 

```
class Video: 
    UUID : <ASIN>
    Type: 
    metadata: Metadata
    AssociatedContent: Video[]
    RawContent: Content[]

```

```
class Metadata: 
    length: Duration 
    encoding: ENUM_ENCODING (1080p, 4K etc)
    # systemRequirements:

```

```
// Vended by a service 
class Content: ?? 
    location: CDN (s3 bucket) (distributed storage)
```


```
class ContentProviderRequest 
    TimeRange
    Quality_requested
    
```

## Rest of the stuff 




--- 

Is better to inherit or compose? 

Composition is generally better than inheritance, 
rule of thumb : achieve polymorphism via composition rather than inheritance
