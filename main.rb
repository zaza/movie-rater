require 'rubygems'
require 'hpricot'
#http://theshed.hezmatt.org/mattshacks/ruby-mechanize/trunk/GUIDE.txt
require 'mechanize'
require 'find'
require 'item'
#require 'unicode'

def readFile(f)
  movies = {}
  # path -> parsed name, rating
  if File.exist?(f)
    File.open(f) do |file|
      while line=file.gets
        # TODO: parse
        puts line
      end
    end
  end
  #movies["moon"] = Item.new("moon","moon",9)
  return movies
end

def extractMovieName(dir)
  # TODO: remove (2009), R5, DVD, etc
  dir = dir.downcase
  dir = dir.gsub('.',' ')
  dir = dir.gsub('_',' ')
  dir = dir.gsub('-diamond','')
  dir = dir.gsub(/\[.*\]/,'')
  dir = dir.gsub(/\{.*\}/,'')
  dir = dir.gsub(/\(.*\)/,'')
  dir = dir.gsub('ac3','')
  dir = dir.gsub('r5','')
  dir = dir.gsub('dvdrip','')
  dir = dir.gsub('dvd','')
  dir = dir.gsub('po polsku','')
  dir = dir.gsub('lektor','')
  dir = dir.gsub(' pl','')
  dir = dir.gsub('rip','')
  dir = dir.gsub('xvid','')
  dir = dir.gsub('ac3','')
  dir = dir.gsub('rmvbhunters','')
  dir = dir.gsub('rmvb','')
  dir = dir.gsub('avi','')
  dir = dir.gsub('dub','')
  dir = dir.gsub(' 3d','')
  dir = dir.gsub(' scr','')
  dir = dir.gsub('bbs','')
  dir = dir.gsub('-fxg','')
  dir = dir.gsub('-fxm','')
  dir = dir.gsub('-hefty','')
  dir = dir.gsub('-axxo','')
  dir = dir.gsub('-xan-0','')
  dir = dir.gsub('-konik','')
  dir = dir.gsub('-80m','')
  dir = dir.gsub('-ruby','')
  dir = dir.gsub('-kjs','')
  dir = dir.gsub('-bestdivx','')
  dir = dir.gsub('proper','')
  dir = dir.gsub('untouched','')
  dir = dir.gsub('domino','')
  dir = dir.gsub('x264-cinefile','')
  dir = dir.gsub('limited','')
  dir = dir.gsub('thiller','')
  dir = dir.gsub('horror','')
  dir = dir.gsub('komedia rom','')
  dir = dir.gsub('720p','')
  dir = dir.gsub('bluray','')
  dir = dir.gsub('1996','')
  dir = dir.gsub('1997','')
  dir = dir.gsub('2001','')
  dir = dir.gsub('2002','')
  dir = dir.gsub('2003','')
  dir = dir.gsub('2004','')
  dir = dir.gsub('2005','')
  dir = dir.gsub('2006','')
  dir = dir.gsub('2007','')
  dir = dir.gsub('2008','')
  dir = dir.gsub('2009','')
  dir = dir.gsub('-',' ')
  return dir.strip
end

class String
  def to_ascii
    ascii = "acelnoszzACELNOSZZ"
    cep = "\271\346\352\263\361\363\234\277\237\245\306\312\243\321\323\214\257\217"
#    s = Iconv.new("cp1250", "UTF-8").iconv(self)
    self.tr!(cep,ascii)
  end
end

def scanDirs(path)
  dirs_hash = {}
  excludes = ARGV[1].split(",")
  if File.directory?(path)
    Find.find(path) do |f|
      if f =~ /^d:\/temp\/movies\/([a-zA-Z-]+)\/(.+)$/
        if !excludes.include?($1)
          dirs = dirs_hash[$1]
          if dirs.nil?
            dirs = []
          end
          d = $2
#          d = Unicode.normalize_KD($2).gsub(/[^\x00-\x7F]/n,'')
#          d = Iconv.iconv('Windows-1250', 'utf-8', $2)
          dirs << d#.to_ascii
          dirs_hash[$1] = dirs 
        end
      end
    end
  end
  p dirs_hash
  return dirs_hash
end

def getMovieRating(dirs_hash)
 
  url = 'http://www.filmweb.pl'
  agent = WWW::Mechanize.new
  page = agent.get(url)
  link = page.links.find{ |l| l.text == 'przejdź do Filmweb.pl' }
  if !link.nil?
    page = agent.click(link)
  end  
  
  link = page.links.find{ |l| l.text == 'Zaloguj się' }
  page = agent.click(link)
  
  credentials = ARGV[0].split('@')
  
  zaloguj_form = page.form('form')
  zaloguj_form.j_username = credentials[0]
  zaloguj_form.j_password = credentials[1]
  page = agent.submit(zaloguj_form)
  
  movies_hash = {}
  
  dirs_hash.each_pair do |category,v|
    puts "======================================> #{category}"
    for dir in v
  
      dir2 = extractMovieName(dir)
      puts "Looking for '" + dir2 +"'..."
  
      szukaj_form = page.forms[0]
      szukaj_form.q = dir2
      szukaj_form.c = 'film'
      page = agent.submit(szukaj_form)
 
      firstResult_a = page.search("//a[@class='searchResultTitle']")[0]
      if firstResult_a.nil?
        i = Item.new(dir, '', 0, category)
      else
        title = firstResult_a.inner_html
        # this might by 'aka' section
        span = page.search("//span[@style='color:#333; font-size: 0.9em; text-decoration: none;']")[0]
        text = span.inner_html
        if text =~ /(ocena:)([0-9]{1}\.[0-9]{1,2})/
          i = Item.new(dir, title, $2, category)
        else
          span = page.search("//span[@style='color:#333; font-size: 0.9em; text-decoration: none;']")[1]
          if span.nil?
            i = Item.new(dir, '', 0, category)
          else
            text = span.inner_html
            if text =~ /(ocena:)([0-9]{1}\.[0-9]{1,2})/
              i = Item.new(dir, title, $2, category)
            end  
          end
        end
      end
      puts i
      movies_hash[dir] = i 
    end
  end  
  return movies_hash
end

movies = readFile("movies.txt")
puts "movies=" + movies.size.to_s
dirs_hash = scanDirs("d:/temp/movies")
puts "categories=" + dirs_hash.size.to_s
  # check in 'movies' first
movies = getMovieRating(dirs_hash)
#  movies[d] = i
  # if not found check on filmweb
    # if found add to 'movies.txt' with rating
    # else add to 'movies.txt' without rating
  # sort by rating, those with rating first
  # dump movies to file
  # print out results
puts "Done."